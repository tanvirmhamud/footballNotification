package com.example.backgroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.backgroundservice.Api_Interface.LiveMatch.Livematchinterface
import com.example.backgroundservice.Model.Live.LivematchItem
import com.example.backgroundservice.Notification.Livematchnotification.Notification2
import com.example.backgroundservice.Retrofit_heloer.Retrofithelper
import com.example.footballnotification.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.sql.Date
import java.sql.Timestamp


class Mybackground() : Service() {
    lateinit var  handler : Handler;
    var goal : Boolean = false;
    var card : Boolean = false;
    var subset : Boolean = false;
    lateinit var token: String;
    lateinit var matchid : ArrayList<Int>;
    var livematchdata = "livematchdata";
    lateinit var context: Context

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        context = this;
        goal = intent!!.getBooleanExtra("goal", false);
        card = intent!!.getBooleanExtra("card", false);
        subset = intent!!.getBooleanExtra("subst", false);
        token = intent!!.getStringExtra("token").toString();
        matchid = intent.getIntegerArrayListExtra("matchid") as ArrayList<Int>
        println("${goal} ${token} ${matchid}")
        var t = 0
        handler = Handler();
        handler.postDelayed(object : Runnable {
            override fun run() {
                CoroutineScope(Dispatchers.Main).async {
                    counter();
                }
                t++
                handler.postDelayed(this, 10000)
            }
        }, 10000)

        val CHANNELID = "Foreground Service ID"
        val channel = NotificationChannel(
            CHANNELID,
            CHANNELID,
            NotificationManager.IMPORTANCE_NONE
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: Notification.Builder = Notification.Builder(this, CHANNELID)
            .setContentText("Background Service ")
            .setContentTitle("Background Servoce")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true);
        startForeground(1001, notification.build())
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(1001)
        return super.onStartCommand(intent, flags, startId)
    }



    suspend fun counter() {
            var job1 = CoroutineScope(Dispatchers.Main).async {
                for (ids in matchid) {
                    val quotesApi =
                        Retrofithelper.getInstance().create(Livematchinterface::class.java)
                    var result = quotesApi.getQuotes("id=${ids}", token).body();
                    var job2 = CoroutineScope(Dispatchers.Main).async {
                        if (result!!.first().fixture.status.short == "NS"){
                            matchstartnotification(result!!.first())
                        }

                        if (result!!.first().events.isNotEmpty()) {
                            goalnotification(result!!.first());
                        }
                        "job2 round complete"
                    }
                    println("${job2.await()}")

                }
                "job1 complete"
            }
            println("${job1.await()}")



//        var job1 = CoroutineScope(Dispatchers.Main).async {
//            val quotesApi = Retrofithelper.getInstance().create(Livematchinterface::class.java)
//            var result = quotesApi.getQuotes(token).body();
//                for (item in result!!.indices){
//                    var job2 = CoroutineScope(Dispatchers.Main).async {
//                        if (result[item].events.isNotEmpty()){
//                            goalnotification(result[item]);
//                        }
//                        "round complete"
//                    }
//                    println("${job2.await()}")
//                }
//            result.size
//        }
//        println(job1.await());
    }


     fun savedata(key: String, value: String) {
        val settings = applicationContext.getSharedPreferences(livematchdata, 0)
        val editor = settings.edit()
        editor.putString(key,value)
        editor.apply()
    }

     fun getsavedata(key: String): String? {
         val settings = applicationContext.getSharedPreferences(livematchdata, 0)
         return settings.getString(key,"0");
    }


    fun goalnotification(livematch : LivematchItem) {
        var goaldata : String = "${livematch.fixture.id}";
        if (getsavedata(goaldata) == null){
           savedata(goaldata, livematch.events.size.toString())
        }else{
            if (getsavedata(goaldata) ==  livematch.events.size.toString()){
                println("previous data")
                savedata(goaldata, livematch.events.size.toString())
            }else{
                var type = livematch.events.last().type;
                if (type == "Goal" && goal == true) {
                    var details = "${livematch.teams.home.name} ${livematch.goals.home} - ${livematch.goals.away} ${livematch.teams.away.name}"
                    Notification2().createNotificationChannel(context,"⚽️ $type",details,livematch.league.logo)
                    savedata(goaldata,  livematch.events.size.toString())
                }else if (type == "Card" && card == true){
                    var details = "${livematch.events.last().player.name} ${livematch.events.last().detail}"
                    Notification2().createNotificationChannel(context,"$type",details,livematch.league.logo)
                    savedata(goaldata,  livematch.events.size.toString())
                }else if(type == "subst" && subset == true) {
                    var details = "${livematch.events.last().detail}"
                    Notification2().createNotificationChannel(context,"$type",details,livematch.league.logo)
                    savedata(goaldata,  livematch.events.size.toString())
                }
                println("new data data")
            }

        }

    }


    fun matchstartnotification(livematch : LivematchItem) {
        val matchstamp = Timestamp((livematch.fixture.timestamp).toLong()) // from java.sql.timestamp
        val matchdate = Date(matchstamp.time * 1000)

        val stamp = Timestamp(System.currentTimeMillis()) // from java.sql.timestamp
        val date = Date(stamp.time)

        val diff: Long = matchdate.getTime() - date.getTime()
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        println("${days}:${hours}:${minutes}:${seconds}")


        var id : String = "${livematch.fixture.id}";
        var type = "${livematch.teams.home.name} vs ${livematch.teams.away.name}";
        if (minutes < 5 && minutes > 0 && (getsavedata(id) == null || getsavedata(id) != "${livematch.fixture.timestamp}")) {
            var details = "Live Now: ${matchdate.toLocaleString()}"
            Notification2().createNotificationChannel(context,"⚽️ $type",details,livematch.league.logo);
            savedata(id, "${livematch.fixture.timestamp}")
        }

    }



}
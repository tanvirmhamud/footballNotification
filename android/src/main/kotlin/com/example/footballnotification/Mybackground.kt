package com.example.backgroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.backgroundservice.Api_Interface.LiveMatch.Livematchinterface
import com.example.backgroundservice.Model.Live.LivematchItem
import com.example.backgroundservice.Notification.Livematchnotification.LiveNotificationShow
import com.example.backgroundservice.Notification.Livematchnotification.Notification2
import com.example.backgroundservice.Retrofit_heloer.Retrofithelper
import com.example.footballnotification.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class Mybackground() : Service() {

    lateinit var  handler : Handler;
     var goal : Boolean = false;
    var card : Boolean = false;
    var subset : Boolean = false;


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
        println("${goal} ${card} ${subset}")
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
            val quotesApi = Retrofithelper.getInstance().create(Livematchinterface::class.java)
            var result = quotesApi.getQuotes().body();
                for (item in result!!.indices){
                    var job2 = CoroutineScope(Dispatchers.Main).async {
                        if (result[item].events.isNotEmpty()){
                            goalnotification(result[item]);
                        }
                        "round complete"
                    }
                    println("${job2.await()}")
                }

            result.size
        }
        println(job1.await());
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
           savedata(goaldata, livematch.fixture.status.elapsed.toString())
        }else{
            if (getsavedata(goaldata) == livematch.fixture.status.elapsed.toString()){
                println("previous data")
                savedata(goaldata,livematch.fixture.status.elapsed.toString())
            }else{
                var type = livematch.events.last().type;
                if (type == "Goal" && goal == true) {
                    var details = "${livematch.teams.home.name} ${livematch.goals.home} - ${livematch.goals.away} ${livematch.teams.away.name}"
                    Notification2().createNotificationChannel(context,"⚽️ $type",details,livematch.league.logo)
                    savedata(goaldata, livematch.fixture.status.elapsed.toString())
                }else if (type == "Card" && card == true){
                    var details = "${livematch.events.last().player.name} ${livematch.events.last().detail}"
                    Notification2().createNotificationChannel(context,"$type",details,livematch.league.logo)
                    savedata(goaldata, livematch.fixture.status.elapsed.toString())
                }else if(type == "subst" && subset == true) {
                    var details = "${livematch.events.last().detail}"
                    Notification2().createNotificationChannel(context,"$type",details,livematch.league.logo)
                    savedata(goaldata, livematch.fixture.status.elapsed.toString())
                }
                println("new data data")
            }

        }

    }

}
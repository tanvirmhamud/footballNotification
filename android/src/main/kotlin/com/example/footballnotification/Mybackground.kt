package com.example.backgroundservice


import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide

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
import kotlin.random.Random


class Mybackground() : Service() {
    lateinit var  handler : Handler;
    lateinit var runable : Runnable;
    var goal : Boolean = false;
    var card : Boolean = false;
    var subset : Boolean = false;
    lateinit var token: String;
    lateinit var matchid : ArrayList<Int>;
    lateinit var teamid : ArrayList<Int>;
    lateinit var season : ArrayList<Int>;
    var matchstart : Boolean = true;
    var time : Int = 3;

    var livematchdata = "livematchdata";
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "${Random.nextInt(0, 9999999)}ghvas"
    private val description = "Test notification"

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runable)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context: Context = this
        goal = intent!!.getBooleanExtra("goal", true);
        card = intent!!.getBooleanExtra("card", true);
        subset = intent!!.getBooleanExtra("subst", true);
        token = intent!!.getStringExtra("token").toString();
        matchid = intent.getIntegerArrayListExtra("matchid") as ArrayList<Int>
        teamid = intent.getIntegerArrayListExtra("teamid") as ArrayList<Int>
        season = intent.getIntegerArrayListExtra("season") as ArrayList<Int>
        matchstart = intent.getBooleanExtra("matchstart",true)
        time = intent.getIntExtra("time",3);
        println("${goal} ${token} ${matchid} ${teamid} ${season} ${matchstart} ${time}")
        var t = 0
        handler = Handler();
        runable = object: Runnable {
                        override fun run() {
                CoroutineScope(Dispatchers.Main).async {
                    counter();
                }
                t++
                handler.postDelayed(this, 10000)
            }
        }
//        var runable = new Runnable {
//            override fun run() {
//                CoroutineScope(Dispatchers.Main).async {
//                    counter();
//                }
//                t++
//                handler.postDelayed(this, 10000)
//            }
//        }
        handler.postDelayed( runable , 10000)

        val CHANNELID = "Foreground Service ID"
        val channel = NotificationChannel(
            CHANNELID,
            CHANNELID,
            NotificationManager.IMPORTANCE_NONE
        )
        var title : String = "${getAppLable(this)} is running in the background";
        val titleBold: Spannable = SpannableString(title)
        titleBold.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val icon = BitmapFactory.decodeResource(
            this.resources,
            R.mipmap.ic_launcher
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: Notification.Builder = Notification.Builder(this, CHANNELID)
            .setContentText("Tab for details on battery and data usage")
            .setContentTitle(titleBold)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(icon)
            .setAutoCancel(true);
        startForeground(1001, notification.build())
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(1001)
        return super.onStartCommand(intent, flags, startId)
    }


    fun getAppLable(context: Context): CharSequence {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = context.packageManager.getApplicationInfo(
                context.applicationInfo.packageName,
                0
            )
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("TAG", "The package with the given name cannot be found on the system.")
        }
        return if (applicationInfo != null) packageManager.getApplicationLabel(applicationInfo) else "Unknown"
    }






    suspend fun counter() {
                for (ids in matchid) {
                    val quotesApi =
                        Retrofithelper.getInstance().create(Livematchinterface::class.java)
                    var result = quotesApi.getQuotes("id=${ids}", token).body();
                    var job2 = CoroutineScope(Dispatchers.Main).async {
                        if (result!!.first().fixture.status.short == "NS" && matchstart){
                            matchstartnotification(result!!.first())
                        }
                        if (result!!.first().events.isNotEmpty()) {
                            goalnotification(result!!.first());
                        }
                        "job2 round complete"
                    }
                    println("${job2.await()}")

                }

                for (ids in teamid.indices){
                    val quotesApi =
                        Retrofithelper.getInstance().create(Livematchinterface::class.java)
                    var result = quotesApi.getteamfixture("${teamid[ids]}","${season[ids]}",token).body();
                    var job2 = CoroutineScope(Dispatchers.Main).async {
                        for (i in result!!){
                            if (i.fixture.status.short == "NS" && matchstart){
                                matchstartnotification(i)
                            }
                        }
                        "job3 round complete"
                    }
                    println("${job2.await()}")

                }


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
        var leagename: String = livematch.league.name;

        var matchid : Int = livematch.fixture.id;
        var teama : Int = livematch.teams.home.id;
        var teamb : Int = livematch.teams.away.id;
        var teamaname : String = livematch.teams.home.name;
        var teambname : String = livematch.teams.away.name;
        var season : Int = livematch.league.season;

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
                    createNotificationChannel("⚽️ $type",details,livematch.league.logo,leagename,matchid,teama, teamb, teamaname, teambname, season)
                    savedata(goaldata,  livematch.events.size.toString())
                }else if (type == "Card" && card == true){
                    var details = "${livematch.events.last().player.name ?: "someone" } got ${livematch.events.last().detail}"
                   createNotificationChannel("$type",details,livematch.league.logo,leagename,matchid,teama, teamb, teamaname, teambname, season)
                    savedata(goaldata, livematch.events.size.toString())
                }else if(type == "subst" && subset == true) {
                    var details = "${livematch.events.last().player.name  ?: "someone"} ${livematch.events.last().detail}"
                    createNotificationChannel("$type",details,livematch.league.logo,leagename,matchid,teama, teamb, teamaname, teambname, season)
                    savedata(goaldata,  livematch.events.size.toString())
                }
                println("new data data")
            }

        }

    }





    fun matchstartnotification(livematch : LivematchItem) {
        val matchstamp = Timestamp((livematch.fixture.timestamp).toLong()) // from java.sql.timestamp
        val matchdate = Date(matchstamp.time * 1000)
        var leagename: String = livematch.league.name;

        val stamp = Timestamp(System.currentTimeMillis()) // from java.sql.timestamp
        val date = Date(stamp.time)

        val diff: Long = matchdate.getTime() - date.getTime()
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        var matchid : Int = livematch.fixture.id;
        var teama : Int = livematch.teams.home.id;
        var teamb : Int = livematch.teams.away.id;
        var teamaname : String = livematch.teams.home.name;
        var teambname : String = livematch.teams.away.name;
        var season : Int = livematch.league.season;


        var id : String = "${livematch.fixture.id}";

        var type = "${livematch.teams.home.name} vs ${livematch.teams.away.name}";
        if (minutes < time && minutes > 0 && (getsavedata(id) == null || getsavedata(id) != "${livematch.fixture.timestamp}")) {
            var details = "Live Now: ${matchdate.toLocaleString()}"
            createNotificationChannel("⚽️ $type",details,livematch.league.logo,leagename,matchid,teama, teamb, teamaname, teambname, season);
            savedata(id, "${livematch.fixture.timestamp}")
        }

    }




    fun createNotificationChannel( title: String, details: String, photourl : String, leaguename: String, matchid : Int, teama: Int, teamb: Int, teamaname: String, teambname : String, season : Int) {
        val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            println("tanvir")
            var CHANNEL_ID : String = "CHANNEL_ID"
            val name = "Channel Name"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.enableVibration(true)
//
            var number : Int = Random.nextInt(0, 99999999);
            val titleBold: Spannable = SpannableString(title)
            titleBold.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
             val futureTarget = Glide.with(this)
                 .asBitmap()
                 .load(photourl)
                 .submit()
             val bitmap = futureTarget.get()
            val icon: Drawable =
                this.packageManager.getApplicationIcon(this.packageName)

            val launchIntent = packageManager.getLaunchIntentForPackage(this.packageName);
//         var launchIntent : Intent = Intent(context, com.example.backgroundservice.)

            launchIntent!!.putExtra("matchid", matchid);
            launchIntent.putExtra("teama", teama);
            launchIntent.putExtra("teamb", teamb);
            launchIntent.putExtra("teamaname",teamaname);
            launchIntent.putExtra("teambname",teambname);
            launchIntent.putExtra("season", season);


            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(launchIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(number,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
            this.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
             val builder = Notification.Builder(this, CHANNEL_ID)
                 .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle(titleBold)
                 .setContentText(details)
                 .setLargeIcon(bitmap)
                 .setSubText(leaguename)
                 .setChannelId(CHANNEL_ID)
                 .setPriority(Notification.PRIORITY_DEFAULT)
                 .setContentIntent(resultPendingIntent)
                 .setAutoCancel(true).build()



            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(number, builder)

        }





//         with(NotificationManagerCompat.from(context)) {
//             notify(number, builder.build())
//         }
    }


}
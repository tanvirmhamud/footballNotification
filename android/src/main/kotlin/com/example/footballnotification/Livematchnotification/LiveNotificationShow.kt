package com.example.backgroundservice.Notification.Livematchnotification
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide

import com.example.footballnotification.R
import kotlin.random.Random



class Notification2{
     fun createNotificationChannel(context: Context, title: String, details: String, photourl : String, leaguename: String, matchid : Int, teama: Int, teamb: Int, teamaname: String, teambname : String, season : Int) {
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
//             val futureTarget = Glide.with(context)
//                 .asBitmap()
//                 .load(photourl)
//                 .submit()
//             val bitmap = futureTarget.get()
             val icon: Drawable =
                 context.packageManager.getApplicationIcon(context.packageName)

             val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName);
//         var launchIntent : Intent = Intent(context, com.example.backgroundservice.)

             launchIntent!!.putExtra("matchid", matchid);
             launchIntent.putExtra("teama", teama);
             launchIntent.putExtra("teamb", teamb);
             launchIntent.putExtra("teamaname",teamaname);
             launchIntent.putExtra("teambname",teambname);
             launchIntent.putExtra("season", season);


             val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                 // Add the intent, which inflates the back stack
                 addNextIntentWithParentStack(launchIntent)
                 // Get the PendingIntent containing the entire back stack
                 getPendingIntent(number,
                     PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
             }

//             val builder = Notification.Builder(context, CHANNEL_ID)
//                 .setSmallIcon(R.mipmap.ic_launcher)
//                 .setContentTitle(titleBold)
//                 .setContentText(details)
//                 .setSubText(leaguename)
//                 .setChannelId(CHANNEL_ID)
//                 .setPriority(Notification.PRIORITY_DEFAULT)
//                 .setContentIntent(resultPendingIntent)
//                 .setAutoCancel(true).build()
             context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
             var builder = Notification.Builder(context, CHANNEL_ID)
                 .setSmallIcon(R.drawable.ic_launcher_foreground)
                 .setContentTitle("ascascascas")
                 .setContentText("sdvsvsvsdv")
                 .build()

             val notificationManager: NotificationManager =
                 context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)

                 notificationManager.notify(1011, builder)

         }





//         with(NotificationManagerCompat.from(context)) {
//             notify(number, builder.build())
//         }
    }



}
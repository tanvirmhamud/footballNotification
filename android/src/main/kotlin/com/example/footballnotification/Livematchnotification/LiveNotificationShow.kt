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
import com.bumptech.glide.Glide
import com.example.footballnotification.R
import kotlin.random.Random


class Notification2{
    val CHANNELID = "Foreground Service ID"
    @RequiresApi(Build.VERSION_CODES.O)
    val channel = NotificationChannel(
        CHANNELID,
        CHANNELID,
        NotificationManager.IMPORTANCE_NONE
    )

     fun createNotificationChannel(context: Context, title: String, details: String, photourl : String, leaguename: String, matchid : Int, teama: Int, teamb: Int, teamaname: String, teambname : String, season : Int) {
         var number : Int = Random.nextInt(0, 99999999);
         val titleBold: Spannable = SpannableString(title)
         titleBold.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
         val futureTarget = Glide.with(context)
             .asBitmap()
             .load(photourl)
             .submit()
         val bitmap = futureTarget.get()
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

         val builder = NotificationCompat.Builder(context, CHANNELID)
             .setSmallIcon(R.mipmap.ic_launcher)
             .setContentTitle(titleBold)
             .setContentText(details)
             .setLargeIcon(bitmap)
             .setSubText(leaguename)
             .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             .setContentIntent(resultPendingIntent)
             .setAutoCancel(true)
         val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
         builder.setSound(alarmSound)
         builder.setVibrate(longArrayOf(1000, 1000))
         with(NotificationManagerCompat.from(context)) {
             notify(number, builder.build())
         }
    }
}
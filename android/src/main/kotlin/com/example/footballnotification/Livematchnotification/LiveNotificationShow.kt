package com.example.backgroundservice.Notification.Livematchnotification
import android.app.*
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
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

     fun createNotificationChannel(context: Context, title: String, details: String, photourl : String) {
         val titleBold: Spannable = SpannableString(title)
         titleBold.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
         val futureTarget = Glide.with(context)
             .asBitmap()
             .load(photourl)
             .submit()
         val bitmap = futureTarget.get()
         val icon: Drawable =
             context.getPackageManager().getApplicationIcon(context.packageName)
         val builder = NotificationCompat.Builder(context, CHANNELID)
             .setSmallIcon(R.mipmap.ic_launcher)
             .setContentTitle(titleBold)
             .setContentText(details)
             .setLargeIcon(bitmap)
             .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             // Set the intent that will fire when the user taps the notification
             .setAutoCancel(true)

         with(NotificationManagerCompat.from(context)) {
             // notificationId is a unique int for each notification that you must define
             notify(Random.nextInt(0, 99999999), builder.build())
         }
    }
}
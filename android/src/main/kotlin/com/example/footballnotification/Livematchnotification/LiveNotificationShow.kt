package com.example.backgroundservice.Notification.Livematchnotification
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.*
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


class LiveNotificationShow {
     @RequiresApi(Build.VERSION_CODES.O)
     fun showNotification(context : Context, title: String, details: String, photourl : String) {

         val channelId = "12345"
         val description = "Test Notification"
         val titleBold: Spannable = SpannableString(title)
         titleBold.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)

         val futureTarget = Glide.with(context)
             .asBitmap()
             .load(photourl)
             .submit()
         val bitmap = futureTarget.get()
//        val largeIcon = resources.getDrawable(R.drawable.ic_launcher_background)
        var builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("sdvsdvsdv")
            .setContentText("sdvsvsvsdv")
            .build()
        with(NotificationManagerCompat.from(context)){
            notify(Random.nextInt(0, 100000), builder);
        }
    }
//    private fun getCircleBitmap(bitmap: Bitmap): Bitmap? {
//        val output = Bitmap.createBitmap(
//            bitmap.width,
//            bitmap.height, Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(output)
//        val color = Color.RED
//        val paint = Paint()
//        val rect = Rect(0, 0, bitmap.width, bitmap.height)
//        val rectF = RectF(rect)
//        paint.setAntiAlias(true)
//        canvas.drawARGB(0, 0, 0, 0)
//        paint.setColor(color)
//        canvas.drawOval(rectF, paint)
//        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
//        canvas.drawBitmap(bitmap, rect, rect, paint)
//        bitmap.recycle()
//        return output
//    }

}



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
         val builder = NotificationCompat.Builder(context, CHANNELID)
             .setSmallIcon(R.drawable.ic_launcher_foreground)
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
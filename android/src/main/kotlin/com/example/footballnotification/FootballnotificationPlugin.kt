package com.example.footballnotification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.example.backgroundservice.Api_Interface.LiveMatch.Livematchinterface
import com.example.backgroundservice.Mybackground
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FootballnotificationPlugin */
class FootballnotificationPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var activity: Activity
  lateinit var notificationManager: NotificationManager
  lateinit var notificationChannel: NotificationChannel
  lateinit var builder: Notification.Builder
  private val channelId = "i.apps.notifications"
  private val description = "Test notification"

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "footballnotification")
    channel.setMethodCallHandler(this)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      var goal : Boolean? = call.argument("goal");
      var card : Boolean? = call.argument("card");
      var subst : Boolean? = call.argument("subst");
      var token : String ? = call.argument("token");
      var matchid : ArrayList<Int>? = call.argument("matchid")
      var teamid : ArrayList<Int>? = call.argument("teamid")
      var season : ArrayList<Int>? = call.argument("season")

//      var intent: Intent = Intent();
      context = activity.applicationContext;
      if (isMyServiceRunning(Mybackground::class.java) == false){
        var service = Intent(context, Mybackground::class.java)
        service.putExtra("goal",goal);
        service.putExtra("card",card);
        service.putExtra("subst",subst);
        service.putExtra("token",token);
        service.putExtra("matchid",matchid);
        service.putExtra("teamid",teamid);
        service.putExtra("season",season);
        activity.startService(service)
      }else{
        var service = Intent(context, Mybackground::class.java)
        activity.stopService(service);
        service.putExtra("goal",goal);
        service.putExtra("card",card);
        service.putExtra("subst",subst);
        service.putExtra("token",token);
        service.putExtra("matchid",matchid);
        service.putExtra("teamid",teamid);
        service.putExtra("season",season);
        activity.startService(service)
      }


    } else {

    }
  }







  private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
    val manager: ActivityManager =activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
      if (serviceClass.name == service.service.getClassName()) {
        return true
      }
    }
    return false
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    context = binding.applicationContext
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.d("TAG", "onAttachedToService");
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.d("TAG", "onAttachedToService");
  }

  override fun onDetachedFromActivity() {
    Log.d("g", "onAttachedToService");
  }

}

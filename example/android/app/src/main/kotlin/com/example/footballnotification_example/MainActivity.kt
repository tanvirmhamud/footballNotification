package com.example.footballnotification_example

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

class MainActivity: FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println(intent.getStringExtra("teamaname"))
        //If app context can be accessed via method, we dont need to assign it to a field.
    }





}

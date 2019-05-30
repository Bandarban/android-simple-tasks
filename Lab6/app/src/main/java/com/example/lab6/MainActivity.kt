package com.example.lab6

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.view.View
import android.widget.Button

var isDead: Boolean = false
var isHungry: Boolean = false
var isDegedrated: Boolean = false


class MainActivity : AppCompatActivity() {
    private var button: Button? = null
    private var button2: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        val intent = Intent(this, MyService::class.java)
        startService(intent)

    }

    fun feed(view: View) {
        isHungry = false
    }

    fun giveWater(view: View) {
        isDegedrated = false

    }


}
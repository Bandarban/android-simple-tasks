package com.example.lab6

import android.annotation.SuppressLint
import android.app.IntentService
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
        val intent = Intent(this, BackgroundIntendService::class.java)
        startService(intent)

    }

    fun feed(view: View) {
        isHungry = false
    }

    fun giveWater(view: View) {
        isDegedrated = false

    }


}

class BackgroundIntendService : IntentService(BackgroundIntendService::class.simpleName) {
    val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onHandleIntent(workIntent: Intent) {
        // Gets data from the incoming Intent
        val dataString = workIntent.dataString
        // Do work here, based on the contents of dataString
        runnable = Runnable {
            if (isHungry or isDegedrated) {
                isDead = true
                var builder = NotificationCompat.Builder(this, "100000")
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("ЖИВОТНОЕ ПОГИБЛО")
                    .setContentText("ТЫ ПОГУБИЛ ЖИВОТНОЕ, ЖИВОТНОЕ!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(100000, builder.build())
                }
            } else {
                isHungry = true
                isDegedrated = true
                var builder = NotificationCompat.Builder(this, "20000")
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("ЖИВОТНОЕ ТРЕБУЕТ ТВОЕГО ВНИМАНИЯ")
                    .setContentText("Дай воды и еды животному, не будь животным!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(20000, builder.build())
                }
            }

            handler.postDelayed(runnable, 10000)
        }
        handler.postDelayed(runnable, 10000)

    }
}

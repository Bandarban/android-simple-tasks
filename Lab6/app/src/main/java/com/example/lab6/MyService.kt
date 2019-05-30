package com.example.lab6

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class MyService : Service() {
    val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}

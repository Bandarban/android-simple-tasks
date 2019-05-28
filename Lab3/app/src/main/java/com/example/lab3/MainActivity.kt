package com.example.lab3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var startButton: Button? = null
    private var stopButton: Button? = null
    private var restartButton: Button? = null

    private var seconds: TextView? = null
    private var minutes: TextView? = null
    private var hours: TextView? = null

    private var currentSec: Int = 0
    private var currentMin: Int = 0
    private var currentHour: Int = 0
    private var isRunning: Boolean = false
    private var startTime: Long = SystemClock.uptimeMillis()
    private var pauseTime: Long = SystemClock.uptimeMillis()

    private lateinit var runnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        restartButton = findViewById(R.id.restartButton)

        seconds = findViewById(R.id.seconds)
        minutes = findViewById(R.id.minutes)
        hours = findViewById(R.id.hours)


        val handler = Handler()
        runnable = Runnable {
            runOnUiThread {
                if (isRunning) {
                    //Update UI
                    currentSec = ((SystemClock.uptimeMillis() - startTime) / 1000).toInt()

                    currentHour = currentSec / 3600
                    currentMin = (currentSec / 60).rem(60)
                    currentSec = currentSec.rem(60)

                    seconds!!.text = (fixOutput(currentSec.toString()))
                    minutes!!.text = (fixOutput(currentMin.toString()))
                    hours!!.text = (fixOutput(currentHour.toString()))
                }

            }
            handler.postDelayed(runnable, 1)
        }
        handler.postDelayed(runnable, 1)

    }

    fun start(view: View) {
        isRunning = true
        startTime += SystemClock.uptimeMillis() - pauseTime

    }



    private fun fixOutput(text: String): String {
        return if (text.length < 2) {
            "0".plus(text)
        } else text
    }


    fun stop(view: View) {
        isRunning = false
        pauseTime = SystemClock.uptimeMillis()
    }

    fun restart(view: View) {
        startTime = SystemClock.uptimeMillis()
        seconds!!.text = ("00")
        minutes!!.text = ("00")
        hours!!.text = ("00")

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong("pauseTime", pauseTime)
        outState?.putLong("startTime", startTime)
        outState?.putInt("currentSec", currentSec)
        outState?.putInt("currentMin", currentMin)
        outState?.putInt("currentHour", currentHour)
        outState?.putBoolean("isRunning", isRunning)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            pauseTime = savedInstanceState.getLong("pauseTime")
            startTime = savedInstanceState.getLong("startTime")
            currentSec = savedInstanceState.getInt("currentSec")
            currentMin = savedInstanceState.getInt("currentMin")
            currentHour = savedInstanceState.getInt("currentHour")
            isRunning = savedInstanceState.getBoolean("isRunning")
            seconds!!.text = (fixOutput(currentSec.toString()))
            minutes!!.text = (fixOutput(currentMin.toString()))
            hours!!.text = (fixOutput(currentHour.toString()))
        }

    }

}

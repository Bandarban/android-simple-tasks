package com.example.lab4

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    companion object {
        var method: ((i:Int) -> Unit)? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            method = {i->Log.d("qwe", i.toString())

            }

        } else {

        }
    }
    fun asd(){

    }

}

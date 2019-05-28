package com.example.lab2

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.URL


class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        imageView!!.setImageBitmap(image)
        startImageDownloadThread()

    }


    private fun startImageDownloadThread() {
        try {
            Thread {
                //Do Network Request
                val url = URL("https://thispersondoesnotexist.com/image")
                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                image = bmp
                runOnUiThread {
                    //Update UI
                    imageView!!.setImageBitmap(image)
                }
            }.start()
        } catch (e: Exception) { // Catch the download exception
            e.printStackTrace()
        }
    }


    fun like(view: View) {
        startImageDownloadThread()
    }

}

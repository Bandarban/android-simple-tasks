package com.example.lab4

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import org.w3c.dom.Element

class MainActivity : AppCompatActivity(), Names.OnFragmentInteractionListener {
    var namesFragment: Fragment = Names()
    var imagesFragment: Fragment = Images()
    var manage: FragmentManager = supportFragmentManager

    override fun onFragmentInteraction(name: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            (imagesFragment as Images).setNumba(name)
            manage.beginTransaction().replace(R.id.frameLayout, imagesFragment).addToBackStack(null).commit()
        } else {
            (imagesFragment as Images).setNumba(name)
            manage.beginTransaction().replace(R.id.frameLayout3, imagesFragment).addToBackStack(null).commit()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            manage.beginTransaction().replace(R.id.frameLayout, namesFragment).addToBackStack(null).commit()
        } else {
            manage.beginTransaction().replace(R.id.frameLayout2, namesFragment).addToBackStack(null).commit()
            manage.beginTransaction().replace(R.id.frameLayout3, imagesFragment).addToBackStack(null).commit()

        }
    }


}

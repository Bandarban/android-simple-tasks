package com.example.lab4


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class Images : Fragment() {
    private var number: Int = 0
    private lateinit var textView: TextView
    private var viewa: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewa = inflater.inflate(R.layout.fragment_images, container, false)
        // Inflate the layout for this fragment
        textView = viewa!!.findViewById(R.id.textView)
        textView.text = number.toString()
        return viewa
    }

    fun setNumba(numba:Int){
        number = numba
        if (viewa != null){
            textView = viewa!!.findViewById(R.id.textView)
            textView.text = number.toString()
        }


    }

}

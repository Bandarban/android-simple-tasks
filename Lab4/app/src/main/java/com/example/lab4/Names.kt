package com.example.lab4


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout


class Names : Fragment() {
    private lateinit var nameList : LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_names, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameList = view.findViewById(R.id.nameList)
        for (i in 1..20){
            val button = Button(activity)
            button.text = i.toString()
            button.setOnClickListener{click(i)}
            nameList.addView(button)
        }
    }

    private fun click(i:Int){


    }

}

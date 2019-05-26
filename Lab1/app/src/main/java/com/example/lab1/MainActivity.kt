package com.example.lab1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private var editText: EditText? = null
    private var textView: TextView? = null
    private var button: Button? = null
    private var randomValue: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        randomValue = Random.nextInt(1, 100)
    }

    /** Called when the user taps the Send button */
    fun sendPredict(view: View) {
        val predict = editText!!.text
        if (checkInput(predict.toString())) {
            val difference = randomValue!! - predict.toString().toInt()
            if (difference == 0) {
                textView!!.setText(R.string.correct_predict_text)
                button!!.setText(R.string.start_game)
                randomValue = Random.nextInt(0, 100)

            } else button!!.setText(R.string.continue_game)

            if (difference > 0) {
                textView!!.setText(R.string.upper_predict_text)
            }
            if (difference < 0) {
                textView!!.setText(R.string.lower_predict_text)
            }
        } else {
            textView!!.setText(R.string.wrong_input)
        }


    }

    private fun checkInput(input: String): Boolean {
        return try{
            val intInput = input.toInt()
            intInput in 1..100
        } catch (e: Exception) {
            false
        }

    }


}

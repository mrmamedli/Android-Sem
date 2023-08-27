package com.example.lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstButton.setOnClickListener {
            firstButton.visibility = View.GONE
            secondButton.visibility = View.VISIBLE
        }
        secondButton.setOnClickListener {
            secondButton.visibility = View.GONE
            firstButton.visibility = View.VISIBLE
        }
    }
}
package com.bit.temperatureapps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HealthStatusActivity: AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.healthy_status)

        val bpm = intent.getStringExtra("bpm").toString().toInt()
        val temperature = intent.getStringExtra("temperature").toString().toDouble()
        val con1: TextView = findViewById(R.id.stat_conditon1)
        val con2: TextView = findViewById(R.id.stat_condition2)
        var condition: TextView = findViewById(R.id.stat_condition)

        var condition1 : String? = null
        var condition2 : String? = null

        // check temperature
        if (temperature < 36.5) {
            condition1 = "Hypothermia"
        } else if (temperature > 37.5) {
            condition1 = "Fever"
        } else {
            condition1 = "Body temperature is normal"
        }

        // check bpm
        if (bpm < 60) {
            condition2 = "brodycardia"
        } else if (bpm > 100) {
            condition2 = "tachycardia"
        } else {
            condition2 = "Heart rate is Normal"
        }

        con1.text = condition1
        con2.text = condition2

        if (condition1 == "Body temperature is normal" && condition2 == "Heart rate is Normal") {
            condition.text = condition1
        } else {
            condition.text = "Abnormal"
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
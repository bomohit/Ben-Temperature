package com.bit.temperatureapps

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log.d
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.IllegalArgumentException

class HealthStatusActivity: AppCompatActivity() {
    var from : String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.healthy_status)

        val bpm = intent.getStringExtra("bpm").toString().toInt()
        val temperature = intent.getStringExtra("temperature").toString().toDouble()

        try {
            from = intent.getStringExtra("from").toString()
        } catch (e: IllegalArgumentException) {
            d("bomoh", "not from history")
        }

        findViewById<TextView>(R.id.stat_temperature).text = "$temperature °C"
        findViewById<TextView>(R.id.stat_bpm).text = "$bpm Bpm"

        val con1: TextView = findViewById(R.id.stat_conditon1)
        val con2: TextView = findViewById(R.id.stat_condition2)
        var condition: TextView = findViewById(R.id.stat_condition)

        var condition1 : String? = null
        var condition2 : String? = null

        // check temperature
        if (temperature < 36.5) {
            condition1 = "1) Body temperature is low(Hypothermia)"
        } else if (temperature > 37.5) {
            condition1 = "1) Body temperature is high(Fever)"
        } else {
            condition1 = "1) Body temperature is normal"
        }

        // check bpm
        if (bpm < 60) {
            condition2 = "2) Heart rate is low(brodycardia)"
        } else if (bpm > 100) {
            condition2 = "2) Heart rate is high(tachycardia)"
        } else {
            condition2 = "2) Heart rate is normal"
        }

        con1.text = condition1
        con2.text = condition2

        if (condition1 == "Body temperature is normal" && condition2 == "Heart rate is normal") {
            condition.text = "Normal"
            condition.setTextColor(Color.GREEN)
        } else {
            condition.text = "Abnormal"
            condition.setTextColor(Color.RED)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (from.isNullOrEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
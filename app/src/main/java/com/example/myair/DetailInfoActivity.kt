package com.example.myair

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info)

        val stationName = intent.getStringExtra("stationName")
        val sidoName = intent.getStringExtra("sidoName")
        val pm10Value = intent.getStringExtra("pm10Value")?.toInt() ?: 0
        val pm25Value = intent.getStringExtra("pm25Value")
        val dataTime = intent.getStringExtra("dataTime")
        val coValue = intent.getStringExtra("coValue")
        val no2Value = intent.getStringExtra("no2Value")
        val o3Value = intent.getStringExtra("o3Value")
        val so2Value = intent.getStringExtra("so2Value")

        updateUI(stationName, sidoName, pm10Value, pm25Value, dataTime, coValue, no2Value, o3Value, so2Value)
    }

    private fun updateUI(stationName: String?, sidoName: String?, pm10Value: Int, pm25Value: String?, dataTime: String?, coValue: String?, no2Value: String?, o3Value: String?, so2Value: String?) {

        val textViewStationName: TextView = findViewById(R.id.textViewStationName)
        val textViewSidoName: TextView = findViewById(R.id.textViewSidoName)
        val textViewPm10Value: TextView = findViewById(R.id.textViewPm10Value)
        val textViewPm25Value: TextView = findViewById(R.id.textViewPm25Value)
//        val textViewDataTime: TextView = findViewById(R.id.textViewDataTime)
        val textViewCoValue: TextView = findViewById(R.id.textViewCoValue)
        val textViewNo2Value: TextView = findViewById(R.id.textViewNo2Value)
        val textViewO3Value: TextView = findViewById(R.id.textViewO3Value)
        val textViewSo2Value: TextView = findViewById(R.id.textViewSo2Value)

        when {
            pm10Value <= 30 -> textViewPm10Value.setBackgroundColor(Color.GREEN)
            pm10Value <= 80 -> textViewPm10Value.setBackgroundColor(Color.YELLOW)
            pm10Value <= 150 -> textViewPm10Value.setBackgroundColor(Color.RED)
            else -> textViewPm10Value.setBackgroundColor(Color.BLACK)
        }

        textViewStationName.text = "Station Name: $stationName"
        textViewSidoName.text = "City: $sidoName"
        textViewPm10Value.text = "PM10 Value: $pm10Value"
        textViewPm25Value.text = "PM2.5 Value: $pm25Value"
//        textViewDataTime.text = "Data Time: $dataTime"
        textViewCoValue.text = "CO Value: $coValue"
        textViewNo2Value.text = "NO2 Value: $no2Value"
        textViewO3Value.text = "O3 Value: $o3Value"
        textViewSo2Value.text = "SO2 Value: $so2Value"
    }
}

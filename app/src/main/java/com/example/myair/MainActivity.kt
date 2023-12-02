package com.example.myair

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myair.widget.AirsAdapter
import android.widget.Spinner
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {
    private val viewModel: AirViewModel by viewModels()
    private var pageNo = 1
    private var selectedCity = "대구"
    private var previousCity = ""

    private val adapter = AirsAdapter { air ->
        val intent = Intent(this, DetailInfoActivity::class.java)
        intent.putExtra("stationName", air.stationName)
        intent.putExtra("sidoName", air.sidoName)
        intent.putExtra("pm10Value", air.pm10Value)
        intent.putExtra("pm25Value", air.pm25Value)
        intent.putExtra("dataTime", air.dataTime)
        intent.putExtra("coValue", air.coValue)
        intent.putExtra("no2Value", air.no2Value)
        intent.putExtra("o3Value", air.o3Value)
        intent.putExtra("so2Value", air.so2Value)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.startButton)
        val refreshButtonLayout: RelativeLayout = findViewById(R.id.refreshButtonLayout)

        startButton.setOnClickListener {
            startButton.visibility = View.GONE
            findViewById<Spinner>(R.id.spinner).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            refreshButtonLayout.visibility = View.VISIBLE
        }

        refreshButtonLayout.setOnClickListener {
            pageNo = 1
            viewModel.getAir(selectedCity, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0", true)
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        selectedCity = spinner.selectedItem.toString()
        previousCity = selectedCity

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCity = parent.getItemAtPosition(position).toString()
                if (selectedCity != previousCity || pageNo == 1) { // 초기 렌더링을 위한 조건 추가
                    pageNo = 1
                    previousCity = selectedCity
                    viewModel.getAir(selectedCity, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0", true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.getAir(selectedCity, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0", true)
            }
        }

        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (lastVisible >= totalItemCount - 1) {
                    pageNo++
                    viewModel.getAir(selectedCity, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0")
                }
            }
        })

        viewModel.airs.observe(this) { airList ->
            adapter.updateData(airList)
        }
    }
}

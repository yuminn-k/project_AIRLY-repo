package com.example.myair

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
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
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class MainActivity : AppCompatActivity() {
    private val viewModel: AirViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var pageNo = 1
    private var selectedCity = "서울"
    private var previousCity = ""
    private lateinit var progressBar: ProgressBar

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        progressBar = findViewById(R.id.progressBar)
        val startButton: Button = findViewById(R.id.startButton)
        val refreshButtonLayout: RelativeLayout = findViewById(R.id.refreshButtonLayout)
        val logo: ImageView = findViewById(R.id.logo)
        val description: TextView = findViewById(R.id.description)
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)

        val locationButton: Button = findViewById(R.id.locationButton)
        locationButton.setOnClickListener {
            if (locationButton.text == "내 위치") {
                getLocation()
                locationButton.text = "돌아가기"
            } else {
                selectedCity = "서울"
                locationButton.text = "내 위치"
                viewModel.getAir(selectedCity, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0", true)
            }
        }

        startButton.setOnClickListener {
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            constraintSet.clear(R.id.logo, ConstraintSet.BOTTOM)
            constraintSet.connect(R.id.logo, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

            constraintSet.clear(R.id.description, ConstraintSet.BOTTOM)
            constraintSet.connect(R.id.description, ConstraintSet.TOP, R.id.logo, ConstraintSet.BOTTOM)

            constraintSet.clear(R.id.startButton, ConstraintSet.BOTTOM)
            constraintSet.connect(R.id.startButton, ConstraintSet.TOP, R.id.description, ConstraintSet.BOTTOM)

            TransitionManager.beginDelayedTransition(constraintLayout)
            constraintSet.applyTo(constraintLayout)

            startButton.visibility = View.GONE
            findViewById<Spinner>(R.id.spinner).visibility = View.VISIBLE
            findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            refreshButtonLayout.visibility = View.VISIBLE
            locationButton.visibility = View.VISIBLE
        }

        refreshButtonLayout.setOnClickListener {
            pageNo = 1
            progressBar.visibility = View.VISIBLE
            viewModel.getAir(selectedCity, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0", true)
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        selectedCity = spinner.selectedItem.toString()
        previousCity = selectedCity

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCity = parent.getItemAtPosition(position).toString()
                if (selectedCity != previousCity || pageNo == 1) {
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

        val footerTextView: TextView = findViewById(R.id.footer)
        footerTextView.movementMethod = LinkMovementMethod.getInstance()
        val spannableString = SpannableString(footerTextView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val url = "https://github.com/yuminn-k"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        val start = footerTextView.text.indexOf("Github Link")
        val end = start + "Github Link".length
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        footerTextView.text = spannableString

        viewModel.airs.observe(this) { airList ->
            adapter.updateData(airList)
            progressBar.visibility = View.GONE
            // 데이터가 갱신된 후에 adapter.notifyDataSetChanged() 호출
            adapter.notifyDataSetChanged()
        }
    }

    private fun convertAddressToSearchKeyword(adminArea: String): List<String> {
        return when(adminArea) {
            "서울특별시", "인천광역시", "대전광역시", "세종특별자치시", "광주광역시", "부산광역시", "대구광역시", "울산광역시", "제주특별자치도" -> listOf(adminArea.dropLast(1))
            "경기도" -> listOf("경기북부", "경기남부")
            "강원도" -> listOf("강원영서", "강원영동")
            "충청남도" -> listOf("충남")
            "충청북도" -> listOf("충북")
            "전라북도" -> listOf("전북")
            "전라남도" -> listOf("전남")
            "경상북도" -> listOf("경북")
            "경상남도" -> listOf("경남")
            else -> listOf(adminArea)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                // Geocoder를 이용해 위치 정보를 도시 이름으로 변환
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val adminArea: String = addresses[0].adminArea
                    val cityNames: List<String> = convertAddressToSearchKeyword(adminArea)

                    // 변환한 도시 이름으로 미세먼지 정보 요청
                    for (cityName in cityNames) {
                        viewModel.getAir(cityName, pageNo, 10, "json", "ppm6OJaHv0U7JZ4pVs+2+s7LnjIngOoils3jxGg5HjYA4xAB6X3zsl55Zkh80ELDfggU+Xi1hgC5kJTqATFk7g==", "1.0", true)
                    }
                }
            }
        }
    }
}

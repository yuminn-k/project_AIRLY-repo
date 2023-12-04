package com.example.myair

import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myair.model.AirQualityResponse
import com.example.myair.model.Item
import com.example.myair.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AirViewModel: ViewModel() {
    val airs = MutableLiveData<List<Item>>()

    fun getAir(sidoName: String, pageNo: Int, numOfRows: Int, returnType: String, serviceKey: String, ver: String, isNewCity: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            RetrofitClient.airsApi.getAir(sidoName, pageNo, numOfRows, returnType, serviceKey, ver).enqueue(object: Callback<AirQualityResponse> {
                override fun onResponse(call: Call<AirQualityResponse>, response: Response<AirQualityResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val newAirs = it.response.body.items  // 새로운 아이템만 가져옴
                            Log.d("AirViewModel", "New Airs: $newAirs") // Log 추가
                            if (isNewCity) {
                                airs.postValue(newAirs)  // 새로운 도시가 선택된 경우 airs 갱신
                            } else {
                                airs.value?.let { oldAirs ->
                                    val updatedAirs = oldAirs + newAirs  // 기존의 아이템에 새로운 아이템을 추가
                                    airs.postValue(updatedAirs)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                    Log.d("AirViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }
}
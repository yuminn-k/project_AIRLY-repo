package com.example.myair.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myair.R
import com.example.myair.model.AirQualityResponse
import com.example.myair.model.Item

class AirsAdapter(val listener:OnAirSelected): RecyclerView.Adapter<AirsAdapter.AirViewHolder>() {
    private var data = mutableListOf<Item>()

    fun interface OnAirSelected {
        fun onAirSelected(air: Item)
    }

    inner class AirViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvStationName: TextView = view.findViewById(R.id.tv_station_name)
        val tvSidoName: TextView = view.findViewById(R.id.tv_sido_name)

        init {
            view.setOnClickListener {
                listener.onAirSelected(data[adapterPosition])
            }
        }
    }

    fun updateData(airList: List<Item>) {
        this.data.clear()  // 기존의 데이터를 모두 제거
        this.data.addAll(airList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_air, parent, false)
        return AirViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AirViewHolder, position: Int) {
        val air = data[position]
        holder.tvStationName.text = air.stationName
        holder.tvSidoName.text = air.sidoName
    }
}
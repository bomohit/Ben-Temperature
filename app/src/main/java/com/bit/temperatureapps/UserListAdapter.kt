package com.bit.temperatureapps

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bit.temperatureapps.data.User

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var userList = emptyList<User>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val row_id: TextView = itemView.findViewById(R.id.row_id)
        val row_date: TextView = itemView.findViewById(R.id.row_date)
        val row_temperature: TextView = itemView.findViewById(R.id.row_temperature)
        val row_bpm: TextView = itemView.findViewById(R.id.row_bpm)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        Log.d("bomoh", "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.row_id.text = currentItem.id.toString()
        holder.row_date.text = currentItem.time
        holder.row_temperature.text = currentItem.temperature
        holder.row_bpm.text = currentItem.bpm

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<User>) {
        this.userList = user
        notifyDataSetChanged()
    }

}

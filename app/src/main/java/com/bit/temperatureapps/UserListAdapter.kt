package com.bit.temperatureapps

import android.annotation.SuppressLint
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bit.temperatureapps.data.User

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private var userList = emptyList<User>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val row_id: TextView = itemView.findViewById(R.id.row_id)
        val row_time: TextView = itemView.findViewById(R.id.row_time)
        val row_temperature: TextView = itemView.findViewById(R.id.row_temperature)
        val row_bpm: TextView = itemView.findViewById(R.id.row_bpm)
        val row_date: TextView = itemView.findViewById(R.id.row_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        d("bomoh", "onCreateViewHolder")
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]
        val info = currentItem.time.split(" ")
        val date = info[0]
        val time = info[1]

        if (position == 0) {
            holder.row_date.isVisible = true
            holder.row_date.text = date
        } else {
            val p_info = userList[position-1].time.split(" ")
            val p_date = p_info[0]
            // If previous date not the same with current
            if (p_date != date) {
                holder.row_date.isVisible = true
                holder.row_date.text = date
            }else {
                holder.row_date.isVisible = false
            }
        }

        holder.row_id.text = currentItem.id.toString()
        holder.row_time.text = time
        holder.row_temperature.text = currentItem.temperature.toDouble().toString() + "°C"
        holder.row_bpm.text = currentItem.bpm + " Bpm"

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<User>) {
        this.userList = user
        notifyDataSetChanged()
    }

}

package com.example.smartpark.ui.main.fragments

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.smartpark.R
import com.example.smartpark.databinding.FragmentSubscriptionBinding
import com.example.smartpark.model.Subscription
import com.example.smartpark.ui.main.MainActivity

class MyItemRecyclerViewAdapter(
    private val context: Context,
    private val values: List<Subscription>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentSubscriptionBinding) : RecyclerView.ViewHolder(binding.root) {
        val plate: TextView = binding.tvPlate
        val valid: TextView = binding.tvValid
        val status: TextView = binding.status
        val image: ImageView = binding.carImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentSubscriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.plate.text = item.licencePlate
        holder.valid.text = "Valid thru: " + item.validThru.split("T")[0]
        holder.status.text = if (item.isActive) "active" else "expired"
        if (!item.isActive)
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.cardinal))
        if (position%3==1)
            holder.image.setImageResource(R.drawable.blue_car)
        if (position%3==2)
            holder.image.setImageResource(R.drawable.yellow_car)

    }

    override fun getItemCount(): Int = values.size
}

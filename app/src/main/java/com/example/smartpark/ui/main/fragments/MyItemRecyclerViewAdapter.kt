package com.example.smartpark.ui.main.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.smartpark.databinding.FragmentSubscriptionBinding
import com.example.smartpark.model.Subscription

class MyItemRecyclerViewAdapter(
    private val values: List<Subscription>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FragmentSubscriptionBinding) : RecyclerView.ViewHolder(binding.root) {
        val plate: TextView = binding.tvPlate
        val valid: TextView = binding.tvValid
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
        holder.valid.text = item.validThru
    }

    override fun getItemCount(): Int = values.size
}

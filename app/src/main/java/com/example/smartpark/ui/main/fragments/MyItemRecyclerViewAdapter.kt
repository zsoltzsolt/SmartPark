package com.example.smartpark.ui.main.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.smartpark.databinding.FragmentSubscriptionBinding
import com.example.smartpark.model.SubscriptionItem

class MyItemRecyclerViewAdapter(
    private val values: List<SubscriptionItem>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            com.example.smartpark.databinding.FragmentSubscriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}
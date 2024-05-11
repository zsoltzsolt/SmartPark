package com.example.smartpark.ui.main.fragments

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.smartpark.R
import com.example.smartpark.databinding.FragmentSubscriptionBinding
import com.example.smartpark.model.Subscription
import com.example.smartpark.ui.main.MainActivity

class MyItemRecyclerViewAdapter(
    private val context: Context,
    private val values: List<Subscription>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_BUTTON = 1
    private val VIEW_TYPE_TITLE = 2

    inner class ItemViewHolder(val binding: FragmentSubscriptionBinding) : RecyclerView.ViewHolder(binding.root) {
        val plate: TextView = binding.tvPlate
        val valid: TextView = binding.tvValid
        val status: TextView = binding.status
        val image: ImageView = binding.carImage
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btn_addCar)
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_title)
        val button: ImageButton = itemView.findViewById(R.id.btn_back)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = FragmentSubscriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ItemViewHolder(binding)
        } else if (viewType == VIEW_TYPE_BUTTON){
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.button_item_layout, parent, false)
            ButtonViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.title_item_layout, parent, false)
            TitleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            val itemHolder = holder as ItemViewHolder
            val item = values[position]
            itemHolder.plate.text = item.licencePlate
            if (item.validThru != null && item.validThru.length > 9) {
                itemHolder.valid.text = "Valid thru: ${item.validThru.substring(0, 10)}"
            }

            itemHolder.status.text = if (item.isActive) "active" else "expired"
            if (!item.isActive)
                itemHolder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.cardinal))
            if (position % 4 == 1)
                itemHolder.image.setImageResource(R.drawable.blue_car)
            if (position % 4 == 2)
                itemHolder.image.setImageResource(R.drawable.yellow_car)
            if (position % 4 == 3)
                itemHolder.image.setImageResource(R.drawable.car_green)
        } else if (getItemViewType(position) == VIEW_TYPE_BUTTON){
            val buttonHolder = holder as ButtonViewHolder
            buttonHolder.button.setOnClickListener {
                val offerFragment = OfferFragment()
                val fragmentManager = (context as MainActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, offerFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }else{
            val button = holder as TitleViewHolder
            button.button.setOnClickListener {
                val profileFragment = ProfileFragment()
                val fragmentManager = (context as MainActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, profileFragment)
                fragmentTransaction.commit()
            }
        }

    }

    override fun getItemCount(): Int = values.size + 1

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) {
            VIEW_TYPE_TITLE
        }
        else if (position < values.size) {
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_BUTTON
        }
    }
}

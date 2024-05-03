package com.example.smartpark.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartpark.R
import com.example.smartpark.model.SubscriptionItem


class SubscriptionFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subscription_list, container, false)

        val subscriptionItems = listOf(
            SubscriptionItem("1", "Subscription 1"),
            SubscriptionItem("2", "Subscription 2"),
            SubscriptionItem("1", "Subscription 1"),
            SubscriptionItem("2", "Subscription 2"),
            SubscriptionItem("1", "Subscription 1"),
            SubscriptionItem("2", "Subscription 2"),
            SubscriptionItem("1", "Subscription 1"),
            SubscriptionItem("2", "Subscription 2"),
        )

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyItemRecyclerViewAdapter(subscriptionItems)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            SubscriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
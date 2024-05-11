package com.example.smartpark.ui.main.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartpark.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*

class SessionsFragment : Fragment() {

    private lateinit var chart: LineChart
    private lateinit var btnPredict: Button
    private lateinit var btnStartDate: Button
    private lateinit var btnStartTime: Button
    private lateinit var btnEndDate: Button
    private lateinit var btnEndTime: Button
    private lateinit var tvSelectedDateTime: TextView
    private var startDateTime: Calendar? = null
    private var endDateTime: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sessions, container, false)

        chart = view.findViewById(R.id.chart)
        btnPredict = view.findViewById(R.id.btn_predict)
        btnStartDate = view.findViewById(R.id.btn_start_day)
        btnStartTime = view.findViewById(R.id.btn_start_hour)
        btnEndDate = view.findViewById(R.id.btn_end_day)
        btnEndTime = view.findViewById(R.id.btn_end_hour)
        tvSelectedDateTime = view.findViewById(R.id.tv_selected_datetime)

        setupChart()
        setupButtons()

        return view
    }

    private fun setupChart() {
    }

    private fun setupButtons() {
        btnStartDate.setOnClickListener {
            showDatePicker(true)
        }

        btnStartTime.setOnClickListener {
            showTimePicker(true)
        }

        btnEndDate.setOnClickListener {
            showDatePicker(false)
        }

        btnEndTime.setOnClickListener {
            showTimePicker(false)
        }

        btnPredict.setOnClickListener {
            if (startDateTime != null && endDateTime != null) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val message = "Predicting availability from ${dateFormat.format(startDateTime!!.time)} to ${dateFormat.format(endDateTime!!.time)}"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please select start and end date/time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                if (isStartDate) {
                    startDateTime = selectedDate
                } else {
                    endDateTime = selectedDate
                }
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH", Locale.getDefault())
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                if (isStartTime) {
                    startDateTime?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    startDateTime?.set(Calendar.MINUTE, minute)
                    tvSelectedDateTime.text = "Selected Date/Time:\n"  + "  " + "${dateFormat.format(startDateTime!!.time)} -"
                } else {
                    endDateTime?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    endDateTime?.set(Calendar.MINUTE, minute)
                    tvSelectedDateTime.text = "Selected Date/Time:\n"  + "  " + "${dateFormat.format(startDateTime!!.time)} - ${dateFormat.format(endDateTime!!.time)}"
                }
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }
}

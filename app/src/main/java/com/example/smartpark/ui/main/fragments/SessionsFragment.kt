package com.example.smartpark.ui.main.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smartpark.R
import com.example.smartpark.model.ProfileResponse
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import org.json.JSONObject
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
    private val predictViewModel: SessionsViewModel by viewModels()

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChart()
        setupButtons()
        observePredictResponse()
    }

    private fun observePredictResponse() {
        predictViewModel.predictResponse.observe(viewLifecycleOwner, { responseMap ->
            Log.d("PredictResponse", responseMap.toString())
            setupChart(responseMap!!)
        })
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
                val startIsoDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).format(startDateTime!!.time)
                val endIsoDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()).format(endDateTime!!.time)
                predictViewModel.predict(startIsoDateTime, endIsoDateTime)
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

    private fun setupChart(responseMap: Map<String, Double>) {
        chart.clear()

        val entries = mutableListOf<Entry>()

        for ((key, value) in responseMap) {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(key)
            val calendar = Calendar.getInstance()
            calendar.time = date!!

            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            entries.add(Entry(hourOfDay.toFloat(), value.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Availability Percentage")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.WHITE

        val lineData = LineData(dataSet)

        chart.data = lineData
        chart.description.text = "Availability Prediction"
        chart.description.textColor = Color.WHITE
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = HourAxisValueFormatter()
        chart.xAxis.textColor = Color.WHITE
        chart.axisLeft.valueFormatter = PercentAxisValueFormatter()
        chart.axisLeft.textColor = Color.WHITE
        chart.legend.textColor = Color.WHITE
        chart.axisRight.isEnabled = false
        chart.invalidate()
    }



    private class HourAxisValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return String.format("%02d:00", value.toInt())
        }
    }

    private class PercentAxisValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return "${(value * 100).toInt()}%"
        }
    }

}

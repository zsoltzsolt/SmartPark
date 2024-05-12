package com.example.smartpark.ui.main.fragments

import android.app.DatePickerDialog
import android.app.Dialog
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
import androidx.fragment.app.DialogFragment
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
    private lateinit var btnStart: Button
    private lateinit var btnEnd: Button
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
        btnStart = view.findViewById(R.id.btn_start_datetime)
        btnEnd = view.findViewById(R.id.btn_end_datetime)
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
        btnStart.setOnClickListener {
            showDateTimePicker(true)
        }

        btnEnd.setOnClickListener {
            showDateTimePicker(false)
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

    private fun showDateTimePicker(isStartDate: Boolean) {
        val currentDateTime = if (isStartDate) startDateTime ?: Calendar.getInstance() else endDateTime ?: Calendar.getInstance()
        val dateTimePicker = DateTimePickerFragment(currentDateTime) { dateTime ->
            if (isStartDate) {
                startDateTime = dateTime
            } else {
                endDateTime = dateTime
            }
            updateSelectedDateTimeText()
        }
        dateTimePicker.show(requireFragmentManager(), "dateTimePicker")
    }

    private fun updateSelectedDateTimeText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val startText = if (startDateTime != null) dateFormat.format(startDateTime!!.time) else "Not set"
        val endText = if (endDateTime != null) dateFormat.format(endDateTime!!.time) else "Not set"
        tvSelectedDateTime.text = "Selected Start Date/Time: $startText\nSelected End Date/Time: $endText"
    }

    class DateTimePickerFragment(
        private val initialDateTime: Calendar,
        private val onDateTimeSetListener: (Calendar) -> Unit
    ) : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

        private var isDateSet = false

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val year = initialDateTime.get(Calendar.YEAR)
            val month = initialDateTime.get(Calendar.MONTH)
            val day = initialDateTime.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(requireContext(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            isDateSet = true
            initialDateTime.set(Calendar.YEAR, year)
            initialDateTime.set(Calendar.MONTH, month)
            initialDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val hour = initialDateTime.get(Calendar.HOUR_OF_DAY)
            val minute = initialDateTime.get(Calendar.MINUTE)
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }

        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            if (isDateSet) {
                initialDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                initialDateTime.set(Calendar.MINUTE, minute)
                onDateTimeSetListener.invoke(initialDateTime)
            }
        }
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

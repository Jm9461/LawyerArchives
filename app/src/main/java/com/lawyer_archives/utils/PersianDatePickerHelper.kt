package com.lawyer_archives.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.lawyer_archives.R
import saman.zamani.persiandate.PersianDate
import java.util.Locale

object PersianDatePickerHelper {

    fun showPersianDatePicker(
        context: Context,
        currentDate: String = "",
        onDateSelected: (String) -> Unit
    ) {
        // تاریخ فعلی یا تاریخ ورودی
        val now = if (currentDate.isNotEmpty()) {
            try {
                val parts = currentDate.split("/")
                if (parts.size == 3) {
                    val pd = PersianDate()
                    pd.initJalaliDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                    pd
                } else {
                    PersianDate()
                }
            } catch (e: Exception) {
                PersianDate()
            }
        } else {
            PersianDate()
        }
        
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_persian_date_picker, null)
        
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker)
        
        // تنظیم سال شمسی
        yearPicker.minValue = 1390
        yearPicker.maxValue = 1450
        yearPicker.value = now.shYear
        
        // تنظیم ماه‌های شمسی
        val persianMonths = arrayOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
        )
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = now.shMonth
        monthPicker.displayedValues = persianMonths
        
        // تنظیم روز
        dayPicker.minValue = 1
        dayPicker.maxValue = 31
        dayPicker.value = now.shDay
        
        // به‌روزرسانی تعداد روزهای ماه بر اساس ماه انتخابی
        monthPicker.setOnValueChangedListener { _, _, newMonth ->
            dayPicker.maxValue = when (newMonth) {
                in 1..6 -> 31
                in 7..11 -> 30
                12 -> if (isLeapYear(yearPicker.value)) 30 else 29
                else -> 31
            }
        }
        
        // نمایش دیالوگ
        AlertDialog.Builder(context)
            .setTitle("انتخاب تاریخ شمسی")
            .setView(dialogView)
            .setPositiveButton("تایید") { _, _ ->
                val selectedDate = String.format(
                    Locale.getDefault(),
                    "%d/%02d/%02d",
                    yearPicker.value,
                    monthPicker.value,
                    dayPicker.value
                )
                onDateSelected(selectedDate)
            }
            .setNegativeButton("لغو", null)
            .create()
            .show()
    }
    
    // بررسی سال کبیسه شمسی
    private fun isLeapYear(year: Int): Boolean {
        val breaks = intArrayOf(1, 5, 9, 13, 17, 22, 26, 30)
        val gy = year + 621
        var jp = breaks[0]
        var jump = 0
        
        for (j in 1 until breaks.size) {
            val jm = breaks[j]
            jump = jm - jp
            if (year < jm) break
            jp = jm
        }
        
        val n = year - jp
        return (jump - n) < 6 && ((n - 1) % 4 == 0 || (jump - n) == 1)
    }
}
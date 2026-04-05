package com.example.xpensate.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.xpensate.Utils
import com.example.xpensate.data.ExpenseDataBase
import com.example.xpensate.data.dao.ExpenseEntityDao
import com.example.xpensate.data.model.ExpenseSummary
import com.github.mikephil.charting.data.Entry

class StatsViewModel(dao : ExpenseEntityDao): ViewModel() {
    val entries = dao.getAllExpenseByDate()

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        return entries.mapIndexed { index, item ->
            Entry(index.toFloat(), item.total_amount.toFloat())
        }
    }
    fun getMonthlyEntries(data: List<ExpenseSummary>): Pair<List<Entry>, List<String>> {

        val calendar = java.util.Calendar.getInstance()

        // 👉 current month (1–12)
        val currentMonth = calendar.get(java.util.Calendar.MONTH) + 1
        val currentYear = calendar.get(java.util.Calendar.YEAR)

        // 👉 Generate 12 months sliding window
        val months = mutableListOf<Pair<Int, Int>>() // (month, year)
        var month = currentMonth
        var year = currentYear

        repeat(12) {
            months.add(Pair(month, year))
            month--
            if (month == 0) {
                month = 12
                year--
            }
        }

        months.reverse() // oldest → newest

        // 👉 Month names
        val monthNames = listOf(
            "Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"
        )

        val labels = months.map { (m, y) -> "${monthNames[m - 1]}" }

        // 👉 Prepare map for fast lookup
        val calendarTemp = java.util.Calendar.getInstance()
        val monthSum = mutableMapOf<Pair<Int, Int>, Float>()

        data.forEach {
            calendarTemp.timeInMillis = it.date
            val m = calendarTemp.get(java.util.Calendar.MONTH) + 1
            val y = calendarTemp.get(java.util.Calendar.YEAR)

            val key = Pair(m, y)
            monthSum[key] = (monthSum[key] ?: 0f) + it.total_amount.toFloat()
        }

        // 👉 Build entries (ALWAYS 12)
        val entries = months.mapIndexed { index, pair ->
            val value = monthSum[pair] ?: 0f
            Entry(index.toFloat(), value)
        }

        return Pair(entries, labels)
    }

//    fun getDailyEntriesForMonth(
//        entries: List<ExpenseSummary>,
//        selectedMonth: Int,   // 0 = Jan, 1 = Feb ...
//        selectedYear: Int
//    ): Pair<List<Entry>, List<String>> {
//
//        val calendar = java.util.Calendar.getInstance()
//
//        // Filter only selected month + year
//        val filtered = entries.filter {
//            calendar.timeInMillis = it.date
//            val month = calendar.get(java.util.Calendar.MONTH)
//            val year = calendar.get(java.util.Calendar.YEAR)
//            month == selectedMonth && year == selectedYear
//        }
//
//        // Group by DAY (1–31)
//        val grouped = filtered.groupBy {
//            calendar.timeInMillis = it.date
//            calendar.get(java.util.Calendar.DAY_OF_MONTH)
//        }
//
//        val daysInMonth = 31 // you can make dynamic later
//
//        val entriesList = mutableListOf<Entry>()
//        val labels = mutableListOf<String>()
//
//        for (day in 1..daysInMonth) {
//            val total = grouped[day]?.sumOf { it.total_amount } ?: 0.0
//            entriesList.add(Entry((day - 1).toFloat(), total.toFloat()))
//            labels.add(day.toString())
//        }
//
//        return Pair(entriesList, labels)
//    }
}

class StatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
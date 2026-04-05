package com.example.xpensate.features.stats

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.xpensate.R
import com.example.xpensate.data.model.ExpenseEntity
import com.example.xpensate.data.model.ExpenseSummary
import com.example.xpensate.ui.theme.Zinc
import com.example.xpensate.viewmodel.StatsViewModel
import com.example.xpensate.viewmodel.StatsViewModelFactory
import com.example.xpensate.widget.ExpenseTextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun StatsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp,start = 16.dp,end = 16.dp,bottom = 8.dp)){
                Image(painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart).clickable {
                        navController.popBackStack()
                    },
                    colorFilter = ColorFilter.tint(Color.Black)
                )
                ExpenseTextView(
                    text = "Statistics",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Zinc,
                    modifier = Modifier.padding(16.dp).align(Alignment.Center)
                )
                Image(painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    ) {
        val viewModel = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)
        val dataState = viewModel.entries.collectAsState(initial = emptyList<ExpenseSummary>())
        Column(modifier = Modifier.padding(it)){
            val calendar = java.util.Calendar.getInstance()

            val (entries, labels) = viewModel.getMonthlyEntries(dataState.value)

            LineChart(entries = entries, labels = labels)

        }
    }
}

@Composable
fun LineChart(
    entries: List<Entry>,
    labels: List<String>,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    AndroidView(
        factory = {
            LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { view ->

        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val dataSet = LineDataSet(entries, "Expenses").apply {
            color = android.graphics.Color.parseColor("#FF2F7E79")
            lineWidth = 3f
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            setDrawValues(false)
        }

        lineChart.data = com.github.mikephil.charting.data.LineData(dataSet)

        lineChart.xAxis.setAvoidFirstLastClipping(true)

        lineChart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = 11f
            granularity = 1f
            labelRotationAngle = -30f


            valueFormatter =
                object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index in labels.indices) labels[index] else ""
                    }
                }
        }

        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        lineChart.invalidate()
    }
}

@Preview
@Composable
fun StatsScreenPreview() {
    StatsScreen(navController = NavController(LocalContext.current))
}


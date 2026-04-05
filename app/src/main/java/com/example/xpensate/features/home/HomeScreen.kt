package com.example.xpensate.features.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.xpensate.data.model.ExpenseEntity
import com.example.xpensate.ui.theme.Zinc
import com.example.xpensate.viewmodel.HomeViewModel
import com.example.xpensate.viewmodel.HomeViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.xpensate.R
import com.example.xpensate.Utils
import com.example.xpensate.ui.theme.Green
import com.example.xpensate.widget.ExpenseTextView

@Composable
fun HomeScreen(navController: NavController) {
//    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(LocalContext.current)
    )

    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val(nameRow,list,card,topBar,add) = createRefs()

            Image(painter = painterResource(id = R.drawable.ic_topbar),contentDescription= null, modifier = Modifier.constrainAs(topBar){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Box(modifier = Modifier.fillMaxWidth().padding(top = 64.dp, start = 16.dp,end = 16.dp).constrainAs(nameRow){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Column {
                    ExpenseTextView(text = "Good Afternoon", fontSize = 16.sp, color = Color.White)
                    ExpenseTextView(
                        text = "Sandeep Gupta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                    )
            }

            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)
            Log.d("DEBUG_DATA", "LIST SIZE = ${state.value.size}")

            CardItem(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },balance,income,expenses
            )

            TransactionList(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().constrainAs(list){
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)   // ✅ ADD THIS
                height = Dimension.fillToConstraints
             },list = state.value,viewModel
            )

            Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = null, modifier = Modifier
                .constrainAs(add){
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
            }.size(56.dp).background(Zinc, shape = CircleShape).padding(16.dp).clickable {
                navController.navigate("add_expense_screen")
             }
            )
        }
    }
}


@Composable
fun TransactionList(modifier: Modifier,list:List<ExpenseEntity>,viewModel: HomeViewModel) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item{
            Box(modifier = Modifier.fillMaxWidth()){
                ExpenseTextView(
                    text = "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                ExpenseTextView(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        items(list){item->
            TransactionItem(
                title = item.title,
                amount = "$ ${item.amount}",
                icon = viewModel.getItemIcon(item),
                date = Utils.formatDateToHumanReadableForm(item.date),
                color = if(item.type == "Income") Green else Color.Red
            )
        }
    }
}

@Composable
fun TransactionItem(title : String,amount:String,icon:Int,date:String,color:Color) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical=8.dp)) {
        Row {
            Image(
                painter = painterResource(id = icon), contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp)
                ExpenseTextView(text = date, fontSize = 12.sp)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}


@Composable
fun CardItem(modifier: Modifier,balance:String,income:String,expense:String) {
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)

    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                ExpenseTextView(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                ExpenseTextView(
                    text = balance,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f)){
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income
            )
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.ic_expense
            )
        }
    }
}


@Composable
fun CardRowItem(modifier:Modifier,title:String,amount:String,image:Int){
    Column(modifier = modifier) {
        Row{
            Image(painter = painterResource(id = image), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            ExpenseTextView(
                text = title,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}




@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen() {
    HomeScreen(rememberNavController())
}
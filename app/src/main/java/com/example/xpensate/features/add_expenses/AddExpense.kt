package com.example.xpensate.features.add_expenses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.xpensate.R
import com.example.xpensate.Utils
import com.example.xpensate.data.model.ExpenseEntity
import com.example.xpensate.ui.theme.Zinc
import com.example.xpensate.viewmodel.AddExpenseViewModel
import com.example.xpensate.viewmodel.AddExpenseViewModelFactory
import com.example.xpensate.widget.ExpenseTextView
import kotlinx.coroutines.launch

@Composable
fun AddExpense(navController: NavController) {

    val viewModel: AddExpenseViewModel = viewModel(
        factory = AddExpenseViewModelFactory(LocalContext.current)
    )
    val coroutineScope = rememberCoroutineScope()


    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp,start = 16.dp,end = 16.dp)
                .constrainAs(nameRow){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){
                ExpenseTextView(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp).align(Alignment.Center)
                )
                Image(painter = painterResource(id = R.drawable.ic_back),contentDescription = null,modifier = Modifier.align(Alignment.CenterStart).clickable {
                    navController.popBackStack()
                },)
                Image(painter = painterResource(id = R.drawable.dots_menu),contentDescription = null,modifier = Modifier.align(Alignment.CenterEnd))

            }

            DataForm(modifier = Modifier.padding(top = 30.dp,bottom = 80.dp).constrainAs(card){
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onAddExpenseClick = {
                coroutineScope.launch {
                    if(viewModel.addExpense(it)){
                        navController.popBackStack()
                    }
                }
            })
        }
    }
}

@Composable
fun DataForm(modifier: Modifier = Modifier,onAddExpenseClick:(model:ExpenseEntity)->Unit) {

    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember{ mutableStateOf(0L)}
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember  { mutableStateOf("")}
    val type = remember { mutableStateOf("")}

    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(26.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ){

        // Name
        ExpenseTextView(text = "Name", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            placeholder = { Text("Enter expense name") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.default_person),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Amount
        ExpenseTextView(text = "Amount", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            placeholder = { Text("Enter amount") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_upwork),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Date
        ExpenseTextView(text = "Date", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = if (date.value == 0L) "" else Utils.formatDateToHumanReadableForm(date.value),
            onValueChange = {},
            placeholder = { Text("Select date") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        ExpenseTextView(text = "Category", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        ExpenseDropDown(
            listOf("Netflix", "Starbucks", "Paypal", "Upwork")
        ) { category.value = it }
        Spacer(modifier = Modifier.height(16.dp))

        // Type Dropdown
        ExpenseTextView(text = "Type", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))
        ExpenseDropDown(listOf("Income", "Expense")) { type.value = it }
        Spacer(modifier = Modifier.height(24.dp))

        // Add Button
        Button(
            onClick = {
                val model = ExpenseEntity(
                    id = null,
                    title = name.value,
                    amount = amount.value.toDoubleOrNull() ?: 0.0,
                    date = date.value,
                    category = category.value,
                    type = type.value
                )
                onAddExpenseClick(model)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Zinc
            )
        ) {
            Text(text = "Add Expense", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

    // Date Picker
    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(
            onDateSelected = {
                date.value = it
                dateDialogVisibility.value = false
            },
            onDismiss = { dateDialogVisibility.value = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(onDateSelected:(date:Long)->Unit , onDismiss:()->Unit){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis  ?:0L

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate)}){
                ExpenseTextView(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDateSelected(selectedDate)}){
                ExpenseTextView(text = "Cancel")
            }
        },
        ){
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(
    listofItem: List<String>,
    onItemSelected: (item: String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf(listofItem[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it }
    ) {

        TextField(
            value = selectedItem.value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            listofItem.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedItem.value = item
                        expanded.value = false
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddExpensePreview() {
    AddExpense(rememberNavController())
}
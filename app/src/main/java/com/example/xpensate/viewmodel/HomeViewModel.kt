package com.example.xpensate.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.xpensate.R
import com.example.xpensate.Utils
import com.example.xpensate.data.ExpenseDataBase
import com.example.xpensate.data.dao.ExpenseEntityDao
import com.example.xpensate.data.model.ExpenseEntity

class HomeViewModel(dao : ExpenseEntityDao): ViewModel() {
    val expenses = dao.getAllExpenses()

    fun getBalance(list : List<ExpenseEntity>) : String{
        var total = 0.0
        list.forEach {
            if(it.type == "Income"){
                total += it.amount
            } else {
                total -= it.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(total)}"
    }

    fun getTotalExpense(list : List<ExpenseEntity>) : String{
        var totalExpense = 0.0
        list.forEach {
          if(it.type == "Expense"){
              totalExpense += it.amount
          }
        }
        return "$ ${Utils.formatToDecimalValue(totalExpense)}"
    }

    fun getTotalIncome(list : List<ExpenseEntity>) : String{
        var totalIncome = 0.0
        list.forEach {
            if(it.type == "Income"){
                totalIncome += it.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(totalIncome)}"
    }

    fun getItemIcon(item:ExpenseEntity): Int {
        if(item.category == "Salary"){
             return R.drawable.ic_upwork
        } else if(item.category == "Paypal"){
             return R.drawable.ic_paypal
        } else if(item.category == "Netflix"){
             return R.drawable.ic_netflix
        } else if(item.category == "Starbucks"){
             return R.drawable.ic_starbucks
        } else {
             return R.drawable.default_person
        }
    }

}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
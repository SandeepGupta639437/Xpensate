package com.example.xpensate.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.xpensate.data.ExpenseDataBase
import com.example.xpensate.data.dao.ExpenseEntityDao
import com.example.xpensate.data.model.ExpenseEntity

class AddExpenseViewModel(val dao : ExpenseEntityDao): ViewModel() {

    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean{
        try{
            dao.insertExpense(expenseEntity)
            return true;
        }catch(ex: Throwable){
           return false;
        }

    }

}



class AddExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
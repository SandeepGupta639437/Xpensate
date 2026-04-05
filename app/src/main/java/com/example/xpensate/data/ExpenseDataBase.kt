package com.example.xpensate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.xpensate.data.dao.ExpenseEntityDao
import com.example.xpensate.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseEntityDao
    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    InitBasicData(context)
                }
                fun InitBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch{
//                        val dao = getDatabase(context).expenseDao()
//                        dao.insertExpense(ExpenseEntity(0,"Salary",5000.0,System.currentTimeMillis().toString(),"Salary","Income"))
//                        dao.insertExpense(ExpenseEntity(3,"Paypal",2000.0,System.currentTimeMillis().toString(),"Paypal","Income"))
//                        dao.insertExpense(ExpenseEntity(2,"Netflix",500.0,System.currentTimeMillis().toString(),"Netflix","Expense"))
//                        dao.insertExpense(ExpenseEntity(1,"Starbucks",900.0,System.currentTimeMillis().toString(),"Starbucks","Expense"))
                    }
                }
            }).build()
        }
    }
}
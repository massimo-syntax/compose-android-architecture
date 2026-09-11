package com.example.database.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.database.local.dao.CoinsDao
import com.example.database.local.model.CoinsEntity

@Database(entities = [CoinsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getCoinsDao(): CoinsDao
    // singleton pattern builder
    companion object{

        @Volatile
        // @Volatile disallow cashing of variable
        // makes it thread safe
        // threads accessing the variable concurrently will have the same (updated) value
        private var instance: AppDatabase? = null

        // thread safe pattern:
        // limit the access to other threads first
        // if the instance is null
        // or return always the same instance (thread safe singleton)
        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this){
                instance ?: buildDb(context).also{
                    instance = it
                }
            }

        private fun buildDb(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DbConst.APP_DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()

        // just in case
        fun destroyDb(){
            instance = null
        }

    }
}

object DbConst{
    const val APP_DB_NAME = "databse"
    const val COIN_LISTS = "coin_list"
}

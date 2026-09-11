package com.example.database.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Update

@Dao
// base dao to extend, consumes generic
interface BaseDao <in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long

    @Delete
    fun delete(t: T): Int

    @Update
    fun update(t: T): Int

}
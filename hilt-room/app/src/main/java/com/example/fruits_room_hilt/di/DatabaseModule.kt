package com.example.fruits_room_hilt.di

import android.content.Context
import androidx.room.Room
import com.example.fruits_room_hilt.data.local.FruitDao
import com.example.fruits_room_hilt.data.local.FruitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    // to inject the interface fruitDao in -> FruitRepositoryImpl
    @Provides
    fun provideFruitDao(fruitDatabase: FruitDatabase): FruitDao {
        return  fruitDatabase.fruitDao()
    }


    // fruitDao needs also a FruitDatabase
    @Provides
    @Singleton
    fun provideFruitDatabase(@ApplicationContext context: Context): FruitDatabase {
        return Room.databaseBuilder(
            context = context,
            FruitDatabase::class.java,
            "fruit_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

}
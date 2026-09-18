package com.example.database.di

import android.content.Context
import com.example.database.local.AppDatabase
import com.example.database.local.dao.CoinsDao
import com.example.database.local.datasource.LocalDataSource
import com.example.database.local.datasource.LocalDataSourceImpl
import com.example.database.preferences.datasource.PreferencesDatasource
import com.example.database.preferences.datasource.PreferencesDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Singleton
    @Provides
    fun provideSearchDatabase(@ApplicationContext context : Context) =
        AppDatabase.getDatabase(context)

    @Provides
    fun provideCoinsDAO(db: AppDatabase): CoinsDao {
        return db.getCoinsDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule{
    @Binds
    abstract fun bindLocalDataSource(impl: LocalDataSourceImpl) : LocalDataSource

    @Binds
    abstract fun bindPreferencesDataSource(impl: PreferencesDatasourceImpl) : PreferencesDatasource
}

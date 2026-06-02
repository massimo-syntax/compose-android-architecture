package com.example.hiltretrofit.data.repository

import com.example.hiltretrofit.data.model.SimpleModel

interface NetworkRepository {
     suspend fun getData(): List<SimpleModel>
}
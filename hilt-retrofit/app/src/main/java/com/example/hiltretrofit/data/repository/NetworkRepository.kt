package com.example.hiltretrofit.data.repository

import com.example.hiltretrofit.data.model.SimpleModel
import com.example.hiltretrofit.data.remote.RequestParam

interface NetworkRepository {
     suspend fun getDefaultData(): List<SimpleModel>
     suspend fun getDetailsData(param: RequestParam): List<SimpleModel>
}
package com.example.data_remote.dataSource

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.data_remote.api.ScienceApi
import com.example.data_remote.dto.ScienceDto
import jakarta.inject.Inject
import java.io.IOException

class ScienceRemoteDataSource @Inject constructor(
    private val api: ScienceApi
) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun getScienceList(): List<ScienceDto> {
        return try {
            api.getScienceList()
        } catch (e: IOException) {
            //throw RemoteException.Network
            emptyList<ScienceDto>()
        } catch (e: HttpException) {
            //throw RemoteException.Server(e.code())
            emptyList<ScienceDto>()
        }
    }

}
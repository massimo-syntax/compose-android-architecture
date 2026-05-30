package com.example.flowsfromgoogledocs.ui.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowsfromgoogledocs.data.NewsRepository
import com.example.flowsfromgoogledocs.data.model.New
import com.example.flowsfromgoogledocs.data.source.datasource.NewsRemoteDataSource
import com.example.flowsfromgoogledocs.data.source.remote.MyServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainScreenViewModel : ViewModel() {

    private val datasource =
        NewsRemoteDataSource(
            MyServer,
            5000,
            Dispatchers.IO
        )
    private val repo =
        NewsRepository(
            datasource,
            Dispatchers.Default
        )

    private val _news = MutableStateFlow<List<New>>(emptyList())
    val news = _news.asStateFlow()

    init {
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            repo.favoriteLatestNews
                // Intermediate catch operator. If an exception is thrown,
                // catch and update the UI
                .catch { exception ->
                    _news.update {
                        listOf(
                            it.last().copy(
                                title = "ERROR",
                                body = "$exception"
                            )
                        )
                    }
                }
                .collect { favoriteNews ->
                _news.update { favoriteNews }
            }
        }
    }


}
package com.example.fruits_room_hilt.ui.FruitScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fruits_room_hilt.data.model.Fruit
import com.example.fruits_room_hilt.data.repository.fruitRepository.FruitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class FruitUiState(
    val fruits: List<Fruit> = listOf()
)

@HiltViewModel
class FruitViewModel @Inject constructor(val fruitRepository: FruitRepository): ViewModel() {
    companion object {
        private const val MILLIS = 5_000L
    }
    val fruitUiState: StateFlow<FruitUiState> =
        fruitRepository.getAllFruits()
            .map { FruitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = FruitUiState()
            )

    fun saveFruit(fruit: Fruit) {
        viewModelScope.launch {
            fruitRepository.insertFruit(fruit)
        }
    }
}

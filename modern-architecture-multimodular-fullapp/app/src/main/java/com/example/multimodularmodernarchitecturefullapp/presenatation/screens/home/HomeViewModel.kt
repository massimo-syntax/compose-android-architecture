package com.example.multimodularmodernarchitecturefullapp.presenatation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utils.logging.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.postgrest.from
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(SupabaseInternal::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val supabase: SupabaseClient
): ViewModel() {

    init {
        supabase.let{
            AppLogger.d("lafskdfjal",message= "Initialize Supabse client")
            AppLogger.d("lafskdfjal",message= it.supabaseHttpUrl)
            viewModelScope.launch {
                it.accessToken?.let { token ->
                    AppLogger.d("lafskdfjal",message= token().toString() )
                }
            }
        }
    }

    fun supabase(callback:(String)->Unit){
        viewModelScope.launch{
            val items = supabase.from("upcoming")
                .select().decodeList<Upcoming>()
                callback(items.toString())

        }
    }

}

@Serializable
data class Upcoming(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String?,
    val name:String?,
    val description:String?,
    val date:String?,
    val extra:String,
)
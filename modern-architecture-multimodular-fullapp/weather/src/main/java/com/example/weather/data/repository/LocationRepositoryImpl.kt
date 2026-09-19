package com.example.weather.data.repository

import com.example.database.preferences.datasource.PreferencesDatasource
import com.example.utils.Resource
import com.example.weather.data.remote.WeatherAPI
import com.example.weather.data.remote.dto.citynamecoordinates.toDomain
import com.example.weather.domain.model.Coordinates
import com.example.weather.domain.model.Location
import com.example.weather.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds


val locations = listOf(
    Location("Lisbon", 387223, -91393),
    Location("Madrid", 404168, -37038),
    Location("Paris", 488566, 23522),
    Location("Amsterdam", 523676, 49041),
    Location("Berlin", 525200, 134050),
    Location("Rome", 419028, 124964),
    Location("Vienna", 482082, 163738),
    Location("Prague", 500755, 144378),
    Location("Warsaw", 522297, 210122),
    Location("Budapest", 474979, 190402),
    Location("Bucharest", 444268, 261025),
    Location("Chisinau", 470105, 288638)
)


@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val preferences: PreferencesDatasource,
    private val locationApi: WeatherAPI
) : LocationRepository {

    override fun getPopularLocations(): Flow<Resource<List<Location>>> = flow {
        emit(Resource.Loading())
        delay(500.milliseconds)
        emit(Resource.Success(locations))
    }

    override fun selectLocation(location: String?) {
        TODO("Not yet implemented")
    }

    // the lat long is not saved in datastore
    // so we have fun requesting them as well
    override suspend fun getCoordinatesByCityName(name: String): Resource<Coordinates> =
        try{
            val response = locationApi.getCoordinatesByCityName(name)
            val locationData = response.toDomain()
            Resource.Success(locationData)
        }catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "unknown error by query location")
        }


}

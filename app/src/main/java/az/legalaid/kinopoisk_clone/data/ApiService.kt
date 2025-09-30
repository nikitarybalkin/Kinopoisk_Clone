package az.legalaid.kinopoisk_clone.data

import retrofit2.http.GET

interface ApiService {
    @GET("films.json")
    suspend fun getFilms(): FilmsResponse
}
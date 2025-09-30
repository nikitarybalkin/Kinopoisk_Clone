package az.legalaid.kinopoisk_clone.data

class FilmRepositoryImpl(private val apiService: ApiService): FilmRepository {
    override suspend fun getPopularFilms(): List<FilmModel> {
        return apiService.getFilms().films
    }
}
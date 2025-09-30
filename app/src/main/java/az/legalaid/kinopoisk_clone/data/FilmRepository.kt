package az.legalaid.kinopoisk_clone.data

interface FilmRepository {
    suspend fun getPopularFilms(): List<FilmModel>
}
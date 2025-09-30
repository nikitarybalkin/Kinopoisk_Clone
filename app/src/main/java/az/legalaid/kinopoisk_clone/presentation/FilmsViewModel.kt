package az.legalaid.kinopoisk_clone.presentation

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.legalaid.kinopoisk_clone.data.FilmModel
import az.legalaid.kinopoisk_clone.data.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilmsViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    private var allFilms: List<FilmModel>? = null
    private val _genres = MutableStateFlow<List<String>?>(null)
    val genres: StateFlow<List<String>?> = _genres.asStateFlow()

    var selectedGenrePos: Int? = null

    private val _films = MutableStateFlow<List<FilmModel>?>(null)
    val films: StateFlow<List<FilmModel>?> = _films.asStateFlow()

    private val _selectedFilm = MutableStateFlow<FilmModel?>(null)
    val selectedFilm: StateFlow<FilmModel?> = _selectedFilm.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadFilms() {
        if (allFilms == null) {
            viewModelScope.launch {
                try {
                    val loadedFilms = filmRepository.getPopularFilms().sortedBy { it.localized_name }
                    _films.value = loadedFilms
                    allFilms = loadedFilms

                    val listOfGenres: MutableList<String> = mutableListOf()
                    loadedFilms.forEach { film ->
                        film.genres.forEach { genre ->
                            val upperGenre = genre.replaceFirstChar { it.uppercase() }
                            if (!listOfGenres.contains(upperGenre)) {
                                listOfGenres.add(upperGenre)
                            }
                        }
                    }
                    _genres.value = listOfGenres
                } catch (e: Exception) {
                    _error.value = if (e.localizedMessage.contains("Unable to resolve host")) {
                        "Ошибка подключения сети"
                    } else {
                        e.localizedMessage
                    }
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun sortFilmsByGenre(genre: String?) {
        if (genre == null) {
            _films.value = allFilms
        } else {
            _films.value = allFilms?.filter { genre.toLowerCase() in it.genres }
        }
    }

    fun selectFilm(film: FilmModel) {
        _selectedFilm.value = film
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
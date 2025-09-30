package az.legalaid.kinopoisk_clone.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import az.legalaid.kinopoisk_clone.R
import az.legalaid.kinopoisk_clone.databinding.FragmentFilmsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FilmsFragment : Fragment() {

    private lateinit var binding: FragmentFilmsBinding
    private val viewModel by activityViewModel<FilmsViewModel>()
    private var genresAdapter: GenresAdapter? = null
    private var filmsAdapter: FilmsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        viewModel.loadFilms()
        loadAndShowInfo()
    }

    private fun loadAndShowInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect {
                it?.let { errorText ->
                    binding.pbLoading.visibility = View.GONE
                    binding.tvErrorText.text = errorText
                    binding.cError.visibility = View.VISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.films.collect { list ->
                list?.let {
                    binding.pbLoading.visibility = View.GONE
                    binding.cMainContent.visibility = View.VISIBLE
                    if (filmsAdapter == null) {
                        filmsAdapter = FilmsAdapter { film ->
                            viewModel.selectFilm(film)
                            findNavController().navigate(R.id.action_filmsFragment_to_currentFilmFragment)
                        }
                        filmsAdapter?.submitList(it)

                        binding.rvFilms.adapter = filmsAdapter
                    } else {
                        filmsAdapter?.submitList(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.genres.collect { list ->
                list?.let {
                    genresAdapter = GenresAdapter(
                        genres = it,
                        position = viewModel.selectedGenrePos,
                        onPositionChange = { pos -> viewModel.selectedGenrePos = pos }
                    ) { genre ->
                        viewModel.sortFilmsByGenre(genre)
                    }
                    binding.rvGenres.adapter = genresAdapter
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.btRetry.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            binding.cError.visibility = View.GONE
            viewModel.clearError()
            viewModel.loadFilms()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        filmsAdapter = null
        genresAdapter = null

    }
}
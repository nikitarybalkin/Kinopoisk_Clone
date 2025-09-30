package az.legalaid.kinopoisk_clone.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import az.legalaid.kinopoisk_clone.R
import az.legalaid.kinopoisk_clone.data.FilmModel
import az.legalaid.kinopoisk_clone.databinding.FragmentCurrentFilmBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CurrentFilmFragment : Fragment() {

    private lateinit var binding: FragmentCurrentFilmBinding
    private val viewModel by activityViewModel<FilmsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showFilm()
        setOnClickListeners()
    }

    private fun showFilm() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedFilm.collect {
                it?.let { film ->
                    binding.tvTopName.text = film.name
                    Glide
                        .with(binding.root)
                        .load(film.image_url)
                        .transform(CenterCrop(), RoundedCorners(12))
                        .placeholder(R.drawable.im_placeholder)
                        .into(binding.ivPreview)
                    binding.tvName.text = film.localized_name
                    binding.tvRating.text = if (film.rating != null) {
                        "%.1f".format(film.rating)
                    } else {
                        getString(R.string.no_rating)
                    }
                    binding.tvGenreDate.text = getGenresAndDate(film)
                    binding.tvDescription.text = film.description
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.flBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getGenresAndDate(film: FilmModel): String {
        var result = ""
        film.genres.forEach {
            result = result + "${it}, "
        }
        return result + "${film.year} год"
    }
}
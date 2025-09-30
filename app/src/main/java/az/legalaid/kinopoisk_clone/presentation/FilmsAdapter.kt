package az.legalaid.kinopoisk_clone.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import az.legalaid.kinopoisk_clone.R
import az.legalaid.kinopoisk_clone.data.FilmModel
import az.legalaid.kinopoisk_clone.databinding.FilmViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FilmsAdapter(
    private val onClick:(FilmModel) -> Unit
) : ListAdapter<FilmModel, FilmsViewHolder>(FilmsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val binding = FilmViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return FilmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item.image_url.let {
                Glide
                    .with(holder.binding.root)
                    .load(item.image_url)
                    .placeholder(R.drawable.im_placeholder)
                    .transform(CenterCrop(), RoundedCorners(12))
                    .into(ivPreviewOfFilm)
            }

            tvName.text = item.localized_name

            container.setOnClickListener {
                onClick(item)
            }
        }
    }
}

class FilmsViewHolder(val binding : FilmViewBinding) : RecyclerView.ViewHolder(binding.root)

class FilmsDiffCallback : DiffUtil.ItemCallback<FilmModel>() {
    override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
        return oldItem.id == newItem.id
    }
}
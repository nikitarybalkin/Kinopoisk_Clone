package az.legalaid.kinopoisk_clone.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import az.legalaid.kinopoisk_clone.R
import az.legalaid.kinopoisk_clone.databinding.GenreViewBinding

class GenresAdapter(
    private val genres: List<String>,
    private val position: Int?,
    private val onPositionChange: (Int?) -> Unit,
    private val onClick:(String?) -> Unit
) :
    RecyclerView.Adapter<GenresViewHolder>() {

    private var selectedPosition: Int? = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding = GenreViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        with(holder.binding) {
            if (selectedPosition == position) {
                container.setBackgroundColor(holder.binding.root.resources.getColor(R.color.yellow))
            } else {
                container.setBackgroundColor(Color.TRANSPARENT)
            }

            val item = genres[position]
            tvGenre.text = item
            container.setOnClickListener {
                onClick(if (holder.absoluteAdapterPosition == selectedPosition) {
                    null
                } else {
                    item
                })
                val currentPosition = if (holder.absoluteAdapterPosition == selectedPosition) {
                    null
                } else {
                    holder.absoluteAdapterPosition
                }
                val lastSelectedPos = selectedPosition
                selectedPosition = currentPosition
                onPositionChange(selectedPosition)
                lastSelectedPos?.let {
                    notifyItemChanged(it)
                }
                notifyItemChanged(holder.absoluteAdapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = genres.size
}

class GenresViewHolder(val binding : GenreViewBinding) : RecyclerView.ViewHolder(binding.root)
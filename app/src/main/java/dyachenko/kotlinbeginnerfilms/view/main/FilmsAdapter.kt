package dyachenko.kotlinbeginnerfilms.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.FilmsItemBinding
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource.Companion.IMAGE_SITE


class FilmsAdapter(
    private var onItemViewClickListener: FilmsFragment.OnItemViewClickListener?,
    private var onNeedLoadNewPageListener: FilmsFragment.OnNeedLoadNewPageListener?
) :
    RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {
    private var films: List<Film> = listOf()

    fun setFilms(list: List<Film>) {
        films = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.films_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(films[position])

        if (itemCount - position == POSITIONS_LEFT_TO_NEED_LOAD_PAGE) {
            onNeedLoadNewPageListener?.onNeedLoadNewPage()
        }
    }

    override fun getItemCount() = films.size

    fun removeListeners() {
        onItemViewClickListener = null
        onNeedLoadNewPageListener = null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FilmsItemBinding.bind(view)

        fun bind(film: Film) = with(binding) {
            itemView.apply {
                filmsItemTextView.text = film.title

                Picasso
                    .get()
                    .load("${IMAGE_SITE}${film.poster_path}")
                    .into(filmsItemImageView)

                setOnClickListener { onItemViewClickListener?.onItemViewClick(film) }
            }
        }
    }

    companion object {
        const val POSITIONS_LEFT_TO_NEED_LOAD_PAGE = 3
    }
}
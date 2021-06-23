package dyachenko.kotlinbeginnerfilms.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.model.Film

class FilmsAdapter(private var onItemViewClickListener: FilmsFragment.OnItemViewClickListener?) :
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
    }

    override fun getItemCount() = films.size

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(film: Film) {
            itemView.apply {
                findViewById<TextView>(R.id.films_item_text_view).text = film.title
                setOnClickListener { onItemViewClickListener?.onItemViewClick(film) }
            }
        }
    }
}
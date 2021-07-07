package dyachenko.kotlinbeginnerfilms.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.HistoryItemBinding
import dyachenko.kotlinbeginnerfilms.format
import dyachenko.kotlinbeginnerfilms.model.room.HistoryEntity

class HistoryAdapter :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var historyEntities: List<HistoryEntity> = listOf()

    fun setHistoryEntities(list: List<HistoryEntity>) {
        historyEntities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyEntities[position])
    }

    override fun getItemCount() = historyEntities.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = HistoryItemBinding.bind(view)

        fun bind(historyEntity: HistoryEntity) = with(binding) {
            itemView.apply {
                val text = "${historyEntity.viewDate.format()}: ${historyEntity.filmTitle}"
                historyItemTextView.text = text
            }
        }
    }
}
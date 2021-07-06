package dyachenko.kotlinbeginnerfilms.view.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.NotesItemBinding
import dyachenko.kotlinbeginnerfilms.format
import dyachenko.kotlinbeginnerfilms.model.room.NoteEntity

class NotesAdapter :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    private var noteEntities: List<NoteEntity> = listOf()

    fun setNoteEntities(list: List<NoteEntity>) {
        noteEntities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteEntities[position])
    }

    override fun getItemCount() = noteEntities.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NotesItemBinding.bind(view)

        fun bind(noteEntity: NoteEntity) = with(binding) {
            itemView.apply {
                val text = "${noteEntity.createdDate.format()}: ${noteEntity.note}"
                notesItemTextView.text = text
            }
        }
    }
}
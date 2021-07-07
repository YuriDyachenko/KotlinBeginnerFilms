package dyachenko.kotlinbeginnerfilms.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val filmId: Int,
    val filmTitle: String,
    val note: String,
    val createdDate: Date
)

package dyachenko.kotlinbeginnerfilms.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val filmId: Int,
    val filmTitle: String,
    val viewDate: Date
)

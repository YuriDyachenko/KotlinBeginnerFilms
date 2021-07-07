package dyachenko.kotlinbeginnerfilms.model.room

import androidx.room.TypeConverter
import dyachenko.kotlinbeginnerfilms.format
import dyachenko.kotlinbeginnerfilms.parseDate
import java.io.Serializable
import java.util.*

class DataConverter : Serializable {

    @TypeConverter
    fun fromDate(date: Date): String = date.format()

    @TypeConverter
    fun toDate(text: String): Date = text.parseDate()
}
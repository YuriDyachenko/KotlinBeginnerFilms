package dyachenko.kotlinbeginnerfilms.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        HistoryEntity::class,
        NoteEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class DataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun noteDao(): NoteDao
}
package dyachenko.kotlinbeginnerfilms.model.room

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteEntity")
    fun all(): List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE filmId=:filmId")
    fun getDataByFilmId(filmId: Int): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: NoteEntity)

    @Update
    fun update(entity: NoteEntity)

    @Delete
    fun delete(entity: NoteEntity)
}
package dyachenko.kotlinbeginnerfilms.app

import android.app.Application
import androidx.room.Room
import dyachenko.kotlinbeginnerfilms.model.room.DataBase
import dyachenko.kotlinbeginnerfilms.model.room.HistoryDao
import dyachenko.kotlinbeginnerfilms.model.room.NoteDao

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: DataBase? = null
        private const val DB_NAME = "data.db"

        fun getHistoryDao(): HistoryDao {
            return getDb().historyDao()
        }

        fun getNoteDao(): NoteDao {
            return getDb().noteDao()
        }

        private fun getDb(): DataBase {
            if (db == null) {
                synchronized(DataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw
                        IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            DataBase::class.java,
                            DB_NAME
                        ).build()
                    }
                }
            }
            return db!!
        }
    }
}
package dyachenko.kotlinbeginnerfilms.model

interface Repository {
    fun getFilmsFromLocalStorage(): List<Film>
}
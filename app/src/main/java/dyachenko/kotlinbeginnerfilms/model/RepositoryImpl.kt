package dyachenko.kotlinbeginnerfilms.model

class RepositoryImpl : Repository {
    override fun getFilmFromServer(): Film {
        return defFilm()
    }

    override fun getFilmFromLocalStoage(): Film {
        return defFilm()
    }

    private fun defFilm(): Film {
        return Film(
            550, "Fight Club", "Many words",
            "none", 37.269, false
        )
    }
}
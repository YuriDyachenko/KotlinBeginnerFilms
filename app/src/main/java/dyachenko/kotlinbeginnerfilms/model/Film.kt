package dyachenko.kotlinbeginnerfilms.model

data class Film(
    val id: Int?,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val popularity: Double?,
    val adult: Boolean?
)

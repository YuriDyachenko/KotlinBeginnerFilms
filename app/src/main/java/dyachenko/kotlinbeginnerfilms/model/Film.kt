package dyachenko.kotlinbeginnerfilms.model

import java.io.Serializable

data class Film(
    val id: Int?,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val popularity: Double?,
    val adult: Boolean?
) : Serializable

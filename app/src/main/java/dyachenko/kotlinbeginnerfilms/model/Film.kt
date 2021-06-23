package dyachenko.kotlinbeginnerfilms.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val popularity: Double,
    val adult: Boolean
) : Parcelable

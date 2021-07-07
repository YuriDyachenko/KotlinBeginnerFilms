package dyachenko.kotlinbeginnerfilms.model

import java.io.Serializable

interface FilmsListTypeChanging: Serializable {
    fun changed()
}
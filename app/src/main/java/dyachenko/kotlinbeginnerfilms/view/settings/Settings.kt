package dyachenko.kotlinbeginnerfilms.view.settings

import dyachenko.kotlinbeginnerfilms.model.FilmsListType

object Settings {
    const val PREFERENCE_NAME = "Settings"
    const val FILMS_LIST_TYPE_NAME = "FILMS_LIST_TYPE"

    var FILMS_LIST_TYPE = FilmsListType.POPULAR
}
package dyachenko.kotlinbeginnerfilms.model

import dyachenko.kotlinbeginnerfilms.R

enum class FilmsListType(val rId: Int, val apiType: String) {
    POPULAR(R.string.type_popular, RemoteDataSource.TYPE_POPULAR),
    TOP_RATED(R.string.type_top_rated, RemoteDataSource.TYPE_TOP_RATED),
    UPCOMING(R.string.type_upcoming, RemoteDataSource.TYPE_UPCOMING),
    NOW_PLAYING(R.string.type_now_playing, RemoteDataSource.TYPE_NOW_PLAYING)
}
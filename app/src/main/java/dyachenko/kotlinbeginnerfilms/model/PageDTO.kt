package dyachenko.kotlinbeginnerfilms.model

data class PageDTO(
    val page: Int?,
    val results: Array<Film>,
    val total_pages: Int?,
    val total_results: Int?
)

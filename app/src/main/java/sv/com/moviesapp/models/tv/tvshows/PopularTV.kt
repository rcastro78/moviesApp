package sv.com.moviesapp.models.tv.tvshows

data class PopularTV(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)
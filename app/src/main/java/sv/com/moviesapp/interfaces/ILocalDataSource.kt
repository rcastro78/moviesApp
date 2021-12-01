package sv.com.moviesapp.interfaces

import sv.com.moviesapp.models.movie.movies.Popular

interface ILocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun getPopularMovies(lang:String): List<Popular>
    suspend fun saveMovies(movies: List<Popular>)
}
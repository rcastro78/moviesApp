package sv.com.moviesapp.interfaces

import sv.com.moviesapp.models.movie.movies.Popular

interface IRemoteDataSource {
    suspend fun getPopularMovies(apiKey: String,lang:String): List<Popular>
}
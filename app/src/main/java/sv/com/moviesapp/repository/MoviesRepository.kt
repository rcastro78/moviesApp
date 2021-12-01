package sv.com.moviesapp.repository

import sv.com.moviesapp.interfaces.ILocalDataSource
import sv.com.moviesapp.interfaces.IRemoteDataSource
import sv.com.moviesapp.models.movie.movies.Popular
import sv.com.moviesapp.networking.IMoviesService

class MoviesRepository(private val service:IMoviesService){

    lateinit var api_key:String
    lateinit var lang:String
    lateinit var page:String
    lateinit var movie_id:String
    lateinit var query:String
    fun getPopularMovies(api_key:String,lang:String,page:String)=service.getPopularMovies(api_key,lang,page)
    fun getTopMovies(api_key:String,lang:String,page:String)=service.getNowPlayingMovies(api_key,lang,page)
    fun getLatestMovie(api_key:String,lang:String)=service.getLatestMovie(api_key,lang)
    fun getVideoMovie(movie_id:String,api_key:String,lang:String)=service.getVideoMovie(movie_id,api_key,lang)
    fun getMovieDetail(movie_id:String,api_key:String,lang:String)=service.getMovieDetails(movie_id.toInt(),api_key,lang)
    fun getSearchMovies(api_key:String,lang:String,query:String) = service.searchMovies(api_key,lang,query)
    //Series
    fun getPopularSeries(api_key:String,lang:String,page:String)=service.getPopularSeries(api_key,lang,page)
    fun getTopSeries(api_key:String,lang:String,page:String)=service.getTopRatedSeries(api_key,lang,page)
    fun getLatestSerie(api_key:String,lang:String)=service.getLatestSeries(api_key,lang)
    fun getSearchSeries(api_key:String,lang:String,query:String) = service.searchSeries(api_key,lang,query)
}
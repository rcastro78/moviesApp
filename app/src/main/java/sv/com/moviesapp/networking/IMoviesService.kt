package sv.com.moviesapp.networking

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sv.com.moviesapp.models.movie.MovieDetail
import sv.com.moviesapp.models.movie.movies.Popular
import sv.com.moviesapp.models.movie.video.MovieVideo
import sv.com.moviesapp.models.tv.tvshows.LatestTVShow
import sv.com.moviesapp.models.tv.tvshows.LatestTVShowData
import sv.com.moviesapp.models.tv.tvshows.PopularTV
import sv.com.moviesapp.models.tv.tvshows.SeriesDetail

interface IMoviesService {


    //Movies
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int,@Query("api_key") api_key:String,@Query("language") language:String): Call<MovieDetail>
    @GET("movie/latest")
    fun getLatestMovie(@Query("api_key") api_key:String,@Query("language") language:String): Call<MovieDetail>



    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") api_key:String,@Query("language") language:String,@Query("page") page:String):Call<Popular>
    @GET("search/movie")
    fun searchMovies(@Query("api_key") api_key:String,@Query("language") language:String,@Query("query") search:String):Call<Popular>
    @GET("movie/top_rated")
    fun getNowPlayingMovies(@Query("api_key") api_key:String,@Query("language") language:String,@Query("page") page:String):Call<Popular>
    @GET("movie/{movie_id}/videos")
    fun getVideoMovie(@Path("movie_id") movie_id:String,@Query("api_key") api_key:String,@Query("language") language:String):Call<MovieVideo>

    //Series
    @GET("tv/popular")
    fun getPopularSeries(@Query("api_key") api_key:String,@Query("language") language:String,@Query("page") page:String):Call<PopularTV>
    @GET("search/tv")
    fun searchSeries(@Query("api_key") api_key:String,@Query("language") language:String,@Query("query") search:String):Call<PopularTV>
    @GET("tv/top_rated")
    fun getTopRatedSeries(@Query("api_key") api_key:String,@Query("language") language:String,@Query("page") page:String):Call<PopularTV>
    @GET("tv/latest")
    fun getLatestSeries(@Query("api_key") api_key:String,@Query("language") language:String): Call<LatestTVShow>
    @GET("tv/{tv_id}")
    fun getSeriesDetails(@Path("tv_id") id:Int,@Query("api_key") api_key:String,@Query("language") language:String): Call<SeriesDetail>

}
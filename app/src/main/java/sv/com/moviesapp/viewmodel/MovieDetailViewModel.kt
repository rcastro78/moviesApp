package sv.com.moviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.moviesapp.models.movie.MovieDetail
import sv.com.moviesapp.networking.LoadingState
import sv.com.moviesapp.repository.MoviesRepository

class MovieDetailViewModel(private val repo: MoviesRepository
                           , movie_id:String
                           , api_key:String
                           , lang:String) : ViewModel(), Callback<MovieDetail> {



    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _data = MutableLiveData<MovieDetail>()
    val data: LiveData<MovieDetail>
        get() = _data

    init {
        fetchData(movie_id,api_key,lang)
    }

    fun fetchData(movie_id:String,api_key:String,lang:String) {
        _loadingState.postValue(LoadingState.LOADING)
        repo.getMovieDetail(movie_id,api_key, lang).enqueue(this)
    }



    override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
        if (response.isSuccessful) {
            _data.postValue(response.body())
            _loadingState.postValue(LoadingState.LOADED)
        } else {
            _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
        _loadingState.postValue(LoadingState.error(t.message))
    }

}
/*
class MovieDetailViewModel:ViewModel() {
    private var iMoviesService:IMoviesService
    private var movieDetailData:MutableLiveData<MovieDetail>
    private var movieVideoData:MutableLiveData<MovieVideo>
    private var latestMovieDetailData:MutableLiveData<MovieDetail>
    init{
        movieDetailData = MutableLiveData()
        movieVideoData = MutableLiveData()
        latestMovieDetailData = MutableLiveData()
        iMoviesService = APIClient.getMoviesService()!!
    }
    fun getMovieDetailObserver():MutableLiveData<MovieDetail>{
        return movieDetailData
    }
    fun getLatestMovieDetailObserver():MutableLiveData<MovieDetail>{
        return latestMovieDetailData
    }
    fun getMovieVideoObserver():MutableLiveData<MovieVideo>{
        return movieVideoData
    }


    fun getMovieVideo(api_key:String,lang:String,videoId:String){
        iMoviesService.getVideoMovie(videoId,api_key,lang)
            .enqueue(object: Callback<MovieVideo> {
                override fun onResponse(call: Call<MovieVideo>, response: Response<MovieVideo>) {
                    if(response!=null){
                        movieVideoData.postValue(response.body())
                    }else{
                        movieVideoData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<MovieVideo>, t: Throwable) {
                    movieVideoData.postValue(null)
                    Log.d("ERROR-DSG",t.localizedMessage)
                }

            })
    }

    fun getLatestMovie(api_key:String,lang:String){
        iMoviesService.getLatestMovie(api_key,lang)
            .enqueue(object: Callback<MovieDetail> {
                override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                    if(response!=null){
                        latestMovieDetailData.postValue(response.body())
                    }else{
                        latestMovieDetailData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                    latestMovieDetailData.postValue(null)
                    Log.d("ERROR-DSG",t.localizedMessage)
                }

            })
    }

    fun getMovieDetail(id:Int,api_key:String,lang:String){
        iMoviesService.getMovieDetails(id,api_key,lang)
            .enqueue(object: Callback<MovieDetail> {
                override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                    if(response!=null){
                        movieDetailData.postValue(response.body())

                     }else{
                        movieDetailData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                    movieDetailData.postValue(null)
                    Log.d("ERROR-DSG",t.localizedMessage)
                }

            })
    }
}

 */
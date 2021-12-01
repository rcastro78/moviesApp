package sv.com.moviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.moviesapp.models.movie.video.MovieVideo
import sv.com.moviesapp.networking.LoadingState
import sv.com.moviesapp.repository.MoviesRepository

class MovieVideoViewModel(private val repo: MoviesRepository
                          , movie_id:String
                          , api_key:String
                          , lang:String) : ViewModel(), Callback<MovieVideo> {



    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _data = MutableLiveData<MovieVideo>()
    val data: LiveData<MovieVideo>
        get() = _data

    init {
        fetchData(movie_id,api_key,lang)
    }

    fun fetchData(movie_id:String,api_key:String,lang:String) {
        _loadingState.postValue(LoadingState.LOADING)
        repo.getVideoMovie(movie_id,api_key, lang).enqueue(this)
    }



    override fun onResponse(call: Call<MovieVideo>, response: Response<MovieVideo>) {
        if (response.isSuccessful) {
            _data.postValue(response.body())
            _loadingState.postValue(LoadingState.LOADED)
        } else {
            _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<MovieVideo>, t: Throwable) {
        _loadingState.postValue(LoadingState.error(t.message))
    }

}
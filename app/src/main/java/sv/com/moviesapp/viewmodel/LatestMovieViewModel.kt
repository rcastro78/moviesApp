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

class LatestMovieViewModel(private val repo: MoviesRepository) : ViewModel(), Callback<MovieDetail> {


    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _data = MutableLiveData<MovieDetail>()
    val data: LiveData<MovieDetail>
        get() = _data

    init {
        fetchData("bade05e0932c7ac9c9d196254e73e282","es-MX")
    }

    private fun fetchData(api_key:String,lang:String) {
        _loadingState.postValue(LoadingState.LOADING)
        repo.getLatestMovie(api_key, lang).enqueue(this)
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
package sv.com.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.moviesapp.models.tv.tvshows.LatestTVShow
import sv.com.moviesapp.networking.LoadingState
import sv.com.moviesapp.repository.MoviesRepository

class SeriesDetailViewModel(private val repo: MoviesRepository) : ViewModel(), Callback<LatestTVShow> {
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _data = MutableLiveData<LatestTVShow>()
    val data: LiveData<LatestTVShow>
        get() = _data

    init {
        fetchData("bade05e0932c7ac9c9d196254e73e282","es-MX")
    }

    private fun fetchData(api_key:String,lang:String) {
        _loadingState.postValue(LoadingState.LOADING)
        repo.getLatestSerie(api_key, lang).enqueue(this)
    }



    override fun onResponse(call: Call<LatestTVShow>, response: Response<LatestTVShow>) {
        if (response.isSuccessful) {
            _data.postValue(response.body())
            _loadingState.postValue(LoadingState.LOADED)
        } else {
            _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<LatestTVShow>, t: Throwable) {
        _loadingState.postValue(LoadingState.error(t.message))
    }
}
/*
class SeriesDetailViewModel:ViewModel() {
    private var iMoviesService: IMoviesService
    private var latestSeriesDetailData: MutableLiveData<LatestTVShow>
    init {
        latestSeriesDetailData = MutableLiveData()
        iMoviesService = APIClient.getMoviesService()!!
    }
    fun getSeriesDetailObserver():MutableLiveData<LatestTVShow>{
        return latestSeriesDetailData
    }
    fun getLatestSeries(api_key:String,lang:String){
        iMoviesService.getLatestSeries(api_key,lang)
            .enqueue(object: Callback<LatestTVShow> {
                override fun onResponse(call: Call<LatestTVShow>, response: Response<LatestTVShow>) {
                    if(response!=null){
                        latestSeriesDetailData.postValue(response.body())
                    }else{
                        latestSeriesDetailData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<LatestTVShow>, t: Throwable) {
                    latestSeriesDetailData.postValue(null)
                    Log.d("ERROR-DSG",t.localizedMessage)
                }

            })



    }

}*/
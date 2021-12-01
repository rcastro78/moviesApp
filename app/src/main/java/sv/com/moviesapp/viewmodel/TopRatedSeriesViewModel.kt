package sv.com.moviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.moviesapp.models.tv.tvshows.PopularTV


import sv.com.moviesapp.networking.LoadingState
import sv.com.moviesapp.repository.MoviesRepository

class TopRatedSeriesViewModel(private val repo: MoviesRepository) : ViewModel(),
    Callback<PopularTV> {


    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _data = MutableLiveData<PopularTV>()
    val data: LiveData<PopularTV>
        get() = _data

    init {
        fetchData("bade05e0932c7ac9c9d196254e73e282","es-MX", "1")
    }

    private fun fetchData(api_key:String,lang:String,page:String) {
        _loadingState.postValue(LoadingState.LOADING)
        repo.getTopSeries(api_key, lang, page).enqueue(this)
    }



    override fun onResponse(call: Call<PopularTV>, response: Response<PopularTV>) {
        if (response.isSuccessful) {
            _data.postValue(response.body())
            _loadingState.postValue(LoadingState.LOADED)
        } else {
            _loadingState.postValue(LoadingState.error(response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<PopularTV>, t: Throwable) {
        _loadingState.postValue(LoadingState.error(t.message))
    }
}
/*
class TopRatedSeriesViewModel:ViewModel(){
    private var topRatedTVData: MutableLiveData<PopularTV>
    private var iMoviesService: IMoviesService
    init {
        topRatedTVData = MutableLiveData()
        iMoviesService = APIClient.getMoviesService()!!
    }
    fun getTopRatedSeriesObserver(): MutableLiveData<PopularTV> {
        return topRatedTVData
    }
    fun getTopRatedSeries(api_key:String,lang:String,page:String){
        iMoviesService.getTopRatedSeries(api_key,lang,page)
            .enqueue(object: Callback<PopularTV> {
                override fun onResponse(call: Call<PopularTV>, response: Response<PopularTV>) {
                    if(response!=null){
                        topRatedTVData.postValue(response.body())
                        Log.d("SERIES",response.message())
                    }else{
                        topRatedTVData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<PopularTV>, t: Throwable) {
                    topRatedTVData.postValue(null)
                    Log.d("ERROR-DSG",t.localizedMessage)
                }

            })

    }
}

 */

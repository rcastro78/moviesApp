package sv.com.moviesapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.com.moviesapp.models.movie.MovieDetail
import sv.com.moviesapp.models.movie.video.MovieVideo
import sv.com.moviesapp.models.tv.tvshows.SeriesDetail
import sv.com.moviesapp.networking.APIClient
import sv.com.moviesapp.networking.IMoviesService

class TVDetailViewModel:ViewModel() {
    private var iMoviesService: IMoviesService
    private var seriesDetailData: MutableLiveData<SeriesDetail>
    //private var movieVideoData: MutableLiveData<MovieVideo>
    init{
        seriesDetailData = MutableLiveData()
        //movieVideoData = MutableLiveData()
        //latestMovieDetailData = MutableLiveData()
        iMoviesService = APIClient.getMoviesService()!!
    }
    fun getSeriesDetailObserver(): MutableLiveData<SeriesDetail> {
        return seriesDetailData
    }
    fun getSeriesDetail(id:Int,api_key:String,lang:String){
        iMoviesService.getSeriesDetails(id,api_key,lang)
            .enqueue(object: Callback<SeriesDetail> {
                override fun onResponse(call: Call<SeriesDetail>, response: Response<SeriesDetail>) {
                    if(response!=null){
                        seriesDetailData.postValue(response.body())
                    }else{
                        seriesDetailData.postValue(null)
                    }

                }

                override fun onFailure(call: Call<SeriesDetail>, t: Throwable) {
                    seriesDetailData.postValue(null)
                    Log.d("ERROR-DSG",t.localizedMessage)
                }

            })
    }
}
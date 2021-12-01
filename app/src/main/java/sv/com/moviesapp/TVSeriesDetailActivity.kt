package sv.com.moviesapp

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.imgMoviePicture
import kotlinx.android.synthetic.main.activity_movie_detail.txtCategories
import kotlinx.android.synthetic.main.activity_movie_detail.txtLength
import kotlinx.android.synthetic.main.activity_movie_detail.txtMovieDetail
import kotlinx.android.synthetic.main.activity_tvseries_detail.*
import sv.com.moviesapp.viewmodel.MovieDetailViewModel
import sv.com.moviesapp.viewmodel.SeriesDetailViewModel
import sv.com.moviesapp.viewmodel.TVDetailViewModel
import java.util.concurrent.TimeUnit

class TVSeriesDetailActivity : AppCompatActivity() {
    private lateinit var tvViewModel: TVDetailViewModel

    var seriesId:Int=0
    var votes:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvseries_detail)
        seriesId = intent.getIntExtra("seriesId",0)
        votes = intent.getStringExtra("avg")!!

        tvViewModel = ViewModelProvider(this).get(TVDetailViewModel::class.java)

        val tfBold = Typeface.createFromAsset(assets,"fonts/GothamRoundedBold_21016.ttf")
        val tfMedium = Typeface.createFromAsset(assets,"fonts/GothamRoundedMedium_21022.ttf")
        txtCategories.typeface = tfMedium
        txtSerieName.typeface = tfBold
        txtLength.typeface = tfMedium
        txtMovieDetail.typeface = tfMedium
        txtVotesSeries.typeface = tfMedium
        txtVotesSeries.text = votes

        getSeriesDetails(seriesId,"bade05e0932c7ac9c9d196254e73e282","es-MX")
    }

    fun getSeriesDetails(id:Int,api_key:String,lang:String){
        tvViewModel.getSeriesDetailObserver().observe(this,{s->

            val path:String
            if(s.poster_path != null){
                path=s.poster_path
            }else{
                path = s.backdrop_path
            }
            val imageUrl = "http://image.tmdb.org/t/p/w500/$path"
            var gen:String=""
            s.genres.forEach { genre->
                gen +=genre.name+" "
            }
            txtCategories.text = gen
            var count = 0
            s.seasons.forEach { season ->
                count++
            }
            txtLength.text="$count temporada(s)"

            Glide.with(this@TVSeriesDetailActivity)

                .load(imageUrl)
                .placeholder(R.drawable.clapperboard)
                .error(R.drawable.clapperboard)
                .centerCrop()
                //.circleCrop()
                .into(imgMoviePicture)

            txtSerieName.text=s.name
            txtMovieDetail.text = s.overview




        })

        tvViewModel.getSeriesDetail(id,api_key,lang)
    }


    fun convertMinToHHMM(minutes: Int): String {
        val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
        val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
        return String.format("%02d:%02d", hours, remainMinutes)
    }




}
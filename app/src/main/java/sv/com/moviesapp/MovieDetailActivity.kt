package sv.com.moviesapp
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.view.*
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import sv.com.moviesapp.dao.AppDatabase
import sv.com.moviesapp.viewmodel.MovieDetailViewModel
import sv.com.moviesapp.viewmodel.MovieVideoViewModel
import java.util.concurrent.TimeUnit
class MovieDetailActivity : AppCompatActivity() {
    var movieId: Int = 0
    var votes:String=""
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movieId = intent.getIntExtra("movieId", 0)
        votes = intent.getStringExtra("avg")!!
        if(hayConexion()) {
            getMovieDetails(movieId.toString(), "bade05e0932c7ac9c9d196254e73e282", "es-MX")
            getMovieVideo(movieId.toString(), "bade05e0932c7ac9c9d196254e73e282", "es-MX")
        }else{
            getMovieFromDB(movieId.toString())
        }
        val tfBold = Typeface.createFromAsset(assets, "fonts/GothamRoundedBold_21016.ttf")
        val tfMedium = Typeface.createFromAsset(assets, "fonts/GothamRoundedMedium_21022.ttf")
        txtCategories.typeface = tfMedium
        txtMovieName.typeface = tfBold
        txtLength.typeface = tfMedium
        txtMovieDetail.typeface = tfMedium
        txtVotes.typeface = tfMedium
        txtVotes.text = votes
        imbPlay.setOnClickListener {
            imgMoviePicture.visibility = GONE
            videoPlayer.visibility = VISIBLE

        }

    }


    fun getMovieFromDB(id:String){
        db = AppDatabase.getInstance(this.application)
        val data = db.iPopularDAO.getPopularMovies(id)
        txtMovieName.text = data[0].title
        txtCategories.text=""
        txtLength.text="00:00"
    }

    fun getMovieVideo(id: String, api_key: String, lang: String) {
        val vm by viewModel<MovieVideoViewModel> { parametersOf(id, api_key, lang) }
        vm.data.observe(this, { video ->
            if (video.results.isNotEmpty()) {
                val videoId = video.results[0].key
                videoPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(videoId, 0f)
                        youTubePlayer.pause()
                    }

                })

            } else {
                Toast.makeText(applicationContext, "El video no está disponible", Toast.LENGTH_LONG)
                    .show()
                imbPlay.isEnabled = false
            }
        })

    }
    fun getMovieDetails(id: String, api_key: String, lang: String) {
        val vm by viewModel<MovieDetailViewModel>() { parametersOf(id, api_key, lang) }


        vm.data.observe(this, { movie ->
            val path: String
            if (movie.poster_path != null) {
                path = movie.poster_path
            } else {
                path = movie.backdrop_path
            }
            val imageUrl = "http://image.tmdb.org/t/p/w500/$path"
            var gen: String = ""
            movie.genres.forEach { genre ->
                gen += genre.name + " "
            }
            txtCategories.text = gen
            txtLength.text = convertMinToHHMM(movie.runtime)
            Glide.with(this@MovieDetailActivity)
                .load(imageUrl)
                .placeholder(R.drawable.clapperboard)
                .error(R.drawable.clapperboard)
                .centerCrop()
                //.circleCrop()
                .into(imgMoviePicture)

            txtMovieName.text = movie.title
            txtMovieDetail.text = movie.overview
        })
    }
    fun convertMinToHHMM(minutes: Int): String {
        val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
        val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
        return String.format("%02d:%02d", hours, remainMinutes)
    }

    fun hayConexion(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}

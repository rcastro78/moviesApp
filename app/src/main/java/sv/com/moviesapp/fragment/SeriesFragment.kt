package sv.com.moviesapp.fragment

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.imgLatestMovie
import kotlinx.android.synthetic.main.fragment_movies.lblPopulares
import kotlinx.android.synthetic.main.fragment_movies.lblUlt
import kotlinx.android.synthetic.main.fragment_movies.rvMovies
import kotlinx.android.synthetic.main.fragment_movies.rvNowMovies
import kotlinx.android.synthetic.main.fragment_movies.txtMovieLatestName
import kotlinx.android.synthetic.main.fragment_series.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import sv.com.moviesapp.R
import sv.com.moviesapp.adapter.NowPlayingAdapter
import sv.com.moviesapp.adapter.PopularAdapter
import sv.com.moviesapp.adapter.PopularTVAdapter
import sv.com.moviesapp.adapter.TopRatedSeriesAdapter
import sv.com.moviesapp.dao.AppDatabase
import sv.com.moviesapp.models.movie.movies.PopularData
import sv.com.moviesapp.models.tv.tvshows.PopularTVData
import sv.com.moviesapp.viewmodel.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SeriesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var tfBold: Typeface
    lateinit var tfMedium: Typeface
    var popularList:ArrayList<PopularTVData> = ArrayList()
    var nowPlayingList:ArrayList<PopularTVData> = ArrayList()
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfBold = Typeface.createFromAsset(activity?.assets,"fonts/GothamRoundedBold_21016.ttf")
        tfMedium = Typeface.createFromAsset(activity?.assets,"fonts/GothamRoundedMedium_21022.ttf")
         arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_series,container, false)
        return view
    }

      override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
          if(hayConexion()) {
              getPopularSeries()
              getTopSeries()
              getLatestSeriesDetails()
          }else{
              getPopularSeriesDB()
              getTopSeriesDB()
          }
          txtMovieLatestName.typeface = tfBold
          lblPopulares.typeface = tfBold
          lblUlt.typeface = tfBold

          searchViewSeries.setOnQueryTextListener(object :
              android.widget.SearchView.OnQueryTextListener {
              override fun onQueryTextSubmit(p0: String?): Boolean {
                  if(hayConexion())
                      getSeriesByName("bade05e0932c7ac9c9d196254e73e282","es-ES",p0!!)
                  else
                      getPopularSeriesDB(p0!!)
                  return false
              }
              override fun onQueryTextChange(p0: String?): Boolean {
                  if(p0!!.isEmpty())
                      popularList.clear()
                  if(hayConexion())
                      getPopularSeries()
                  else
                      getPopularSeriesDB()
                  return false
              }
          })
    }


    private fun getSeriesByName(api_key:String, lang:String, query:String){
        val vm by viewModel<SearchSeriesViewModel>{ parametersOf(api_key, lang, query) }
        popularList.clear()
        vm.data.observe(
            viewLifecycleOwner,
            {
                it.results.forEach { movie->
                    var bdpath:String=""
                    if(movie.backdrop_path==null || movie.backdrop_path.length<=0){
                        bdpath="https://imag.malavida.com/mvimgbig/download-fs/the-movie-db-25210-0.jpg"
                    }else{
                        val path = movie.backdrop_path
                        bdpath="https://image.tmdb.org/t/p/w500/$path"
                    }

                    var ppath:String=""
                    if(movie.poster_path==null || movie.poster_path.length<=0){
                        bdpath="https://imag.malavida.com/mvimgbig/download-fs/the-movie-db-25210-0.jpg"
                    }else{
                        val path = movie.poster_path
                        ppath="https://image.tmdb.org/t/p/w500/$path"
                    }

                    val m = PopularTVData(movie.id,movie.name,bdpath,ppath,movie.vote_average.toString())
                    popularList.add(m)
                }

                val adapter = PopularTVAdapter(popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                CoroutineScope(Dispatchers.Main).launch {
                    rvMovies.adapter = adapter
                }
            }
        )
    }
    fun getPopularSeries(){
        val popularViewModel by viewModel<PopularSeriesViewModel>()
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            if(!db.iPopularSeriesDAO.getPopularSeries().isEmpty()){
                db.iPopularSeriesDAO.delete()
            }
        }
        popularViewModel.data.observe(
            viewLifecycleOwner,
            {
                it.results.forEach { movie->

                    //Guardar en la base de datos



                    var bdpath:String=""
                    if(movie.backdrop_path==null || movie.backdrop_path.length<=0){
                        bdpath="https://imag.malavida.com/mvimgbig/download-fs/the-movie-db-25210-0.jpg"
                    }else{
                        val path = movie.backdrop_path
                        bdpath="http://image.tmdb.org/t/p/w500/$path"
                    }

                    var ppath:String=""
                    if(movie.poster_path==null || movie.poster_path.length<=0){
                        bdpath="https://imag.malavida.com/mvimgbig/download-fs/the-movie-db-25210-0.jpg"
                    }else{
                        val path = movie.poster_path
                        ppath="https://image.tmdb.org/t/p/w500/$path"
                    }

                    val m = PopularTVData(movie.id,movie.name,bdpath,ppath,movie.vote_average.toString())
                    popularList.add(m)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.iPopularSeriesDAO.insert(m.id, m.name, m.backdrop_path!!, m.poster_path!!,movie.overview)
                    }
                }

                val adapter = PopularTVAdapter(popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                CoroutineScope(Dispatchers.Main).launch {
                    rvMovies.adapter = adapter
                }
            }
        )
    }
    fun getTopSeries(){
        val vm by viewModel<TopRatedSeriesViewModel>()
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            if(!db.iTopSerieDAO.getTopSeries().isEmpty()){
                db.iTopSerieDAO.delete()
            }
        }
        vm.data.observe(
            viewLifecycleOwner,
            {
                it.results.forEach { serie->
                    val s = PopularTVData(serie.id,serie.name,serie.backdrop_path,serie.poster_path,serie.vote_average.toString())
                    CoroutineScope(Dispatchers.IO).launch {
                        //db.iTopSerieDAO.insert(s.id, s.name, s.backdrop_path!!, s.poster_path!!)
                    }
                    nowPlayingList.add(s)

                }

                val adapter = TopRatedSeriesAdapter(nowPlayingList)
                rvNowMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                CoroutineScope(Dispatchers.Main).launch {
                    rvNowMovies.adapter = adapter
                }
            }
        )
    }
    private fun getLatestSeriesDetails(){
        val vm by viewModel<SeriesDetailViewModel>()
        vm.data.observe(
            viewLifecycleOwner,
            {serie->
                val path:String
                if(serie.poster_path != null){
                    path=serie.poster_path
                }else{
                    path = serie.poster_path
                }
                val imageUrl = "http://image.tmdb.org/t/p/w500/$path"

                txtMovieLatestName.text = serie.name

                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.clapperboard)
                    .error(R.drawable.clapperboard)
                    .centerCrop()
                    //.circleCrop()
                    .into(imgLatestMovie)
            })
    }

    private fun getPopularSeriesDB(name:String){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            val popular = db.iPopularSeriesDAO.getSerieByTitle(name)
            popular.forEach{ p->
                val m = PopularTVData(p.id!!.toInt(),p.title!!,p.backdrop_path,p.poster_path,"-")
                popularList.add(m)
            }

            CoroutineScope(Dispatchers.Main).launch {
                val adapter = PopularTVAdapter(popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                rvMovies.adapter = adapter
            }
        }
    }
    private fun getPopularSeriesDB(){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
           val popular = db.iPopularSeriesDAO.getPopularSeries()
           popular.forEach{ p->
               val m = PopularTVData(p.id!!.toInt(),p.title!!,p.backdrop_path,p.poster_path,"-")
               popularList.add(m)
           }

            CoroutineScope(Dispatchers.Main).launch {
                val adapter = PopularTVAdapter(popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                rvMovies.adapter = adapter
            }
        }
    }
    private fun getTopSeriesDB(){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            val top = db.iTopSerieDAO.getTopSeries()
            top.forEach{ p->
                val m = PopularTVData(p.id!!.toInt(),p.title!!,p.backdrop_path,p.poster_path,"-")
                nowPlayingList.add(m)
            }

            CoroutineScope(Dispatchers.Main).launch {
                val adapter = TopRatedSeriesAdapter(nowPlayingList)
                rvNowMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                rvNowMovies.adapter = adapter
            }
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun hayConexion(): Boolean {
        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}
package sv.com.moviesapp.fragment

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_movies.*
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

class MoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var tfBold:Typeface
    lateinit var tfMedium:Typeface
    private var popularList:ArrayList<PopularData> = ArrayList()
    private var nowPlayingList:ArrayList<PopularData> = ArrayList()
    var search:String = ""
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfBold = Typeface.createFromAsset(activity?.assets,"fonts/GothamRoundedBold_21016.ttf")
        tfMedium = Typeface.createFromAsset(activity?.assets,"fonts/GothamRoundedMedium_21022.ttf")
        //txtMovieLatestName.typeface=tfBold



        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies,container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(hayConexion()) {
            getTopMovies()
            getPopularMovies()
            getLatestMovie()
        }else{
            getTopMoviesDB()
            getPopularMoviesDB()
        }
        txtMovieLatestName.typeface = tfBold
        lblPopulares.typeface = tfBold
        lblUlt.typeface = tfBold

        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(hayConexion())
                    getMoviesByName("bade05e0932c7ac9c9d196254e73e282","es-ES",p0!!)
                else
                    getPopularMoviesDB(p0!!)
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0!!.isEmpty())
                    popularList.clear()
                if(hayConexion())
                    getPopularMovies()
                else
                    getPopularMoviesDB()
                return false
            }
        })

    }




    private fun getMoviesByName(api_key:String, lang:String, query:String){
        val vm by viewModel<SearchMoviesViewModel>{ parametersOf(api_key, lang, query) }
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

                    val m = PopularData(movie.id,movie.title,bdpath,ppath,movie.vote_average.toString())
                    popularList.add(m)
                }

                val adapter = PopularAdapter(requireActivity(),popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                CoroutineScope(Dispatchers.Main).launch {
                    rvMovies.adapter = adapter
                }
            }
        )
    }

    private fun getPopularMovies(){
        db = AppDatabase.getInstance(requireActivity().application)
        val popularViewModel by viewModel<PopularViewModel>()
        CoroutineScope(Dispatchers.IO).launch {
        if(!db.iPopularDAO.getPopularMovies().isEmpty()){
                db.iPopularDAO.delete()
            }
        }

        popularViewModel.data.observe(
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
                    val m = PopularData(movie.id,movie.title,bdpath,ppath,movie.vote_average.toString())
                    popularList.add(m)
                     CoroutineScope(Dispatchers.IO).launch {
                        db.iPopularDAO.insert(m.id, m.title, m.backdrop_path, m.poster_path,movie.overview)
                    }

                }

                val adapter = PopularAdapter(requireActivity(),popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                CoroutineScope(Dispatchers.Main).launch {
                    rvMovies.adapter = adapter
                }
            }
        )
    }
    fun getTopMovies(){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
        if(!db.iTopMoviesDAO.getTopMovies().isEmpty()){

                db.iTopMoviesDAO.delete()
            }

        }
        val topRatedViewModel by viewModel<TopRatedViewModel>()
        topRatedViewModel.data.observe(
            viewLifecycleOwner,
            {
                it.results.forEach { movie->

                    val m = PopularData(movie.id,movie.title,movie.backdrop_path,movie.poster_path,movie.vote_average.toString())
                    nowPlayingList.add(m)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.iTopMoviesDAO.insert(m.id, m.title, m.backdrop_path, m.poster_path,movie.overview)
                    }
                }

                val adapter = NowPlayingAdapter(requireActivity(),nowPlayingList)
                rvNowMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                CoroutineScope(Dispatchers.Main).launch {
                    rvNowMovies.adapter = adapter
                }
            }
        )
    }

    fun getPopularMoviesDB(name:String){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            val popular = db.iPopularDAO.getMovieByTitle(name)

            popular.forEach{ p->
                val m = PopularData(p.id!!.toInt(),p.title!!,p.backdrop_path!!,p.poster_path!!,"-")
                popularList.add(m)
            }


            CoroutineScope(Dispatchers.Main).launch {
                val adapter = PopularAdapter(requireActivity(), popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                rvMovies.adapter = adapter
            }
        }
    }
    fun getPopularMoviesDB(){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            val popular = db.iPopularDAO.getPopularMovies()

            popular.forEach{ p->
                val m = PopularData(p.id!!.toInt(),p.title!!,p.backdrop_path!!,p.poster_path!!,"-")
                popularList.add(m)
            }


            CoroutineScope(Dispatchers.Main).launch {
                val adapter = PopularAdapter(requireActivity(), popularList)
                rvMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                rvMovies.adapter = adapter
            }
        }
    }
    private fun getTopMoviesDB(){
        db = AppDatabase.getInstance(requireActivity().application)
        CoroutineScope(Dispatchers.IO).launch {
            val top = db.iTopMoviesDAO.getTopMovies()

            top.forEach{ p->
                val m = PopularData(p.id!!.toInt(),p.title!!,p.backdrop_path!!,p.poster_path!!,"-")
                nowPlayingList.add(m)
            }
            CoroutineScope(Dispatchers.Main).launch {
            val adapter = NowPlayingAdapter(requireActivity(), nowPlayingList)
            rvNowMovies.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            //
                rvNowMovies.adapter = adapter
            }
        }
    }



    private fun getLatestMovie(){
        val vm by viewModel<LatestMovieViewModel>()
            vm.data.observe(
            viewLifecycleOwner,
            {movie->
                val path:String
                if(movie.poster_path != null){
                    path=movie.poster_path
                }else{
                    path = movie.poster_path
                }
                val imageUrl = "http://image.tmdb.org/t/p/w500/$path"


                txtMovieLatestName.text = movie.title

                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.clapperboard)
                    .error(R.drawable.clapperboard)
                    .centerCrop()
                    //.circleCrop()
                    .into(imgLatestMovie)
            })


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
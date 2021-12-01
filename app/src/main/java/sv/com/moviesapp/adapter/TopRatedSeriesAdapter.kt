package sv.com.moviesapp.adapter

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sv.com.moviesapp.MovieDetailActivity
import sv.com.moviesapp.R
import sv.com.moviesapp.TVSeriesDetailActivity
import sv.com.moviesapp.models.tv.tvshows.PopularTVData

class TopRatedSeriesAdapter(private var popularList:ArrayList<PopularTVData>):
    RecyclerView.Adapter<TopRatedSeriesAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMovie  : ImageView = view.findViewById(R.id.imgMovie)
        val txtMovie  : TextView = view.findViewById(R.id.txtMovie)
        val llContainer : LinearLayout = view.findViewById(R.id.llContainerMovie)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = popularList[position]
        val path = p.backdrop_path

        Glide.with(holder.imgMovie.context).load("http://image.tmdb.org/t/p/w500/$path")
            .placeholder(R.drawable.tmdb)
            .error(R.drawable.tmdb)
            .fitCenter()
            .into(holder.imgMovie)
        val tf = Typeface.createFromAsset(
            holder.txtMovie.context.assets,
            "fonts/GothamRoundedMedium_21022.ttf"
        )
        val tfbold =
            Typeface.createFromAsset(holder.txtMovie.context.assets, "fonts/GothamRoundedBold_21016.ttf")
        holder.txtMovie.text = p.name
        holder.txtMovie.typeface = tf

        holder.llContainer.setOnClickListener {
            val intent = Intent(it.context, TVSeriesDetailActivity::class.java)
            intent.putExtra("seriesId",p.id)
            intent.putExtra("avg",p.average)
            it.context.startActivity(intent)
        }


    }
    override fun getItemCount(): Int {
        return popularList.size
    }


}
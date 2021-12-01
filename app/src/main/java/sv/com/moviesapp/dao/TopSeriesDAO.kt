package sv.com.moviesapp.dao

import androidx.room.*

@Entity(tableName = TopSeriesDAO.TABLE_NAME)
class TopSeriesDAO(@PrimaryKey(autoGenerate = true) val uid: Int,
                      @ColumnInfo(name = "id")  val id: String?,
                      @ColumnInfo(name = "title") val title: String?,
                      @ColumnInfo(name = "backdrop_path") val backdrop_path: String?,
                      @ColumnInfo(name = "poster_path") val poster_path: String?,
                      @ColumnInfo(name = "overview") val overview: String?
)
{
    companion object {
        const val TABLE_NAME = "top_series"

    }

    @Dao
    interface ITopSeriesDAO{
        @Query("SELECT * FROM $TABLE_NAME")
        fun getTopSeries(): List<TopSeriesDAO>
        @Query("SELECT * FROM $TABLE_NAME where id=:id")
        fun getTopSeries(id:String): List<TopSeriesDAO>

        @Query("INSERT INTO $TABLE_NAME(id,title,backdrop_path,poster_path,overview) VALUES(:id,:title,:backdrop_path,:poster_path,:overview)")
        fun insert(id: Int, title:String, backdrop_path:String, poster_path:String,overview:String)

        @Query("DELETE FROM $TABLE_NAME")
        fun delete()

        //@Query("SELECT * FROM "+PopularDataDAO.TABLE_NAME+" WHERE title like '%:title%'")
        //fun getMovieByTitle(title:String): List<PopularDataDAO>
    }


}
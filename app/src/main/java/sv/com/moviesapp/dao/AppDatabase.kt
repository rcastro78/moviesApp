package sv.com.moviesapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = arrayOf(PopularDataDAO::class,TopMoviesDAO::class,PopularSerieDAO::class,
TopSeriesDAO::class), version = 1)

    abstract class AppDatabase : RoomDatabase() {

        companion object {
            private var INSTANCE: AppDatabase? = null
            fun getInstance(context: Context): AppDatabase {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "tmdb0a.db")
                        .build()
                }
                return INSTANCE as AppDatabase
            }
        }
    abstract val iPopularDAO: PopularDataDAO.IPopularDataDAO
    abstract val iTopMoviesDAO: TopMoviesDAO.ITopMoviesDAO
    abstract val iPopularSeriesDAO: PopularSerieDAO.IPopularSerieDAO
    abstract val iTopSerieDAO: TopSeriesDAO.ITopSeriesDAO
    }

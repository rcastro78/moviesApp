package sv.com.moviesapp

import android.app.Application

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sv.com.moviesapp.di.*
import java.util.logging.Level

class MoviesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(org.koin.core.logger.Level.DEBUG)
            androidContext(this@MoviesApplication)
            modules(listOf(repositoryModule,
             /*movies*/   viewModelModule,topRatedModule,latestMovieModule,movieDetailModule,movieVideoModule,movieSearchModule,
             /*series*/   popularSeriesViewModule,topSeriesViewModule,latestSerieViewModule,serieSearchModule,
                retrofitModule, apiModule))
        }
    }
}
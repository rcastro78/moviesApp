package sv.com.moviesapp.di

import android.annotation.SuppressLint
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.com.moviesapp.networking.IMoviesService
import sv.com.moviesapp.networking.RetrofitClient
import sv.com.moviesapp.repository.MoviesRepository
import sv.com.moviesapp.viewmodel.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val viewModelModule = module {

    viewModel {
        PopularViewModel(get())
    }

}

val topRatedModule = module {

    viewModel {
        TopRatedViewModel(get())
    }

}

val latestMovieModule = module {

    viewModel {
        LatestMovieViewModel(get())
    }

}

val movieDetailModule = module {
        viewModel {(id:String,api_key:String,lang:String)->
            MovieDetailViewModel(get(),movie_id=id,api_key=api_key,lang=lang)
        }
}

val movieSearchModule = module {
    viewModel {(api_key:String,lang:String,query:String)->
        SearchMoviesViewModel(get(),api_key=api_key,lang=lang,query=query)
    }
}

val movieVideoModule = module {

    viewModel {(id:String,api_key:String,lang:String)->
        MovieVideoViewModel(get(),movie_id=id,api_key=api_key,lang=lang)
    }

}

//Series
val popularSeriesViewModule = module {

    viewModel {
        PopularSeriesViewModel(get())
    }

}
val serieSearchModule = module {
    viewModel {(api_key:String,lang:String,query:String)->
        SearchSeriesViewModel(get(),api_key=api_key,lang=lang,query=query)
    }
}

val topSeriesViewModule = module {

    viewModel {
        TopRatedSeriesViewModel(get())
    }

}

val latestSerieViewModule = module {

    viewModel {
        SeriesDetailViewModel(get())
    }

}


val repositoryModule = module {
    single {
        MoviesRepository(get())
    }
}
val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): IMoviesService {
        return retrofit.create(IMoviesService::class.java)
    }

    single { provideUseApi(get()) }
}

val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }




    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(RetrofitClient.UnsafeOkHttpClient.unsafeOkHttpClient)
            .build()





    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get(), get()) }
}
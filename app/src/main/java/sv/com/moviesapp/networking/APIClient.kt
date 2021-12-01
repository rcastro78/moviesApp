package sv.com.moviesapp.networking

class APIClient {
    companion object {
        private const val WEBSERVICE_URL = "https://api.themoviedb.org/3/"
        fun getMoviesService(): IMoviesService? {
            return RetrofitClient.getClient(WEBSERVICE_URL)?.create(IMoviesService::class.java)
        }

    }
}
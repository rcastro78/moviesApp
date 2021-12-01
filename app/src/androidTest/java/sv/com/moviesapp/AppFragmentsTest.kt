package sv.com.moviesapp

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.fragment.app.testing.launchFragmentInContainer
import sv.com.moviesapp.fragment.MoviesFragment
import sv.com.moviesapp.fragment.SeriesFragment

@RunWith(AndroidJUnit4::class)
class AppFragmentsTest {


    @Test
    fun testTopMoviesFragment() {
        val scenario = launchFragmentInContainer<MoviesFragment>()
        scenario.onFragment{f->
            f.getTopMovies()
        }

    }
    @Test
    fun testTopSeriesFragment() {
        val scenario = launchFragmentInContainer<SeriesFragment>()
        scenario.onFragment{f->
            f.getTopSeries()
        }

    }
    @Test
    fun testMoviesFragment() {
        val scenario = launchFragmentInContainer<MoviesFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)

    }

    @Test
    fun testSeriesFragment() {
        val scenario = launchFragmentInContainer<SeriesFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)

    }

}
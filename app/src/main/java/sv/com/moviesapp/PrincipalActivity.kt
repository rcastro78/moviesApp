package sv.com.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_principal.*
import sv.com.moviesapp.adapter.TabAdapter
import sv.com.moviesapp.fragment.MoviesFragment
import sv.com.moviesapp.fragment.SeriesFragment

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        val mSectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager
        )
        container.setAdapter(mSectionsPagerAdapter)
        val adapter = TabAdapter(supportFragmentManager)
        adapter.addFragment(MoviesFragment(), "Movies")
        adapter.addFragment(SeriesFragment(), "Series")

        container.setAdapter(adapter)
        tabs.setupWithViewPager(container)


    }



}


class PlaceholderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}


class SectionsPagerAdapter(fm: FragmentManager?) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return PlaceholderFragment.newInstance(position + 1)
    }

    override fun getCount(): Int {
        return 2
    }
}

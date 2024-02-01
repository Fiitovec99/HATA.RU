import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.hataru.R
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.Roomtypes
import com.example.hataru.presentation.fragments.FlatFragment
import com.example.hataru.presentation.fragments.FlatFragment.Companion.KEY_GET_FLAT_INTO_FLATFRAGMENT
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class ApartmentsViewPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var roomtypesList: List<Roomtype>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_apartments_view_pager, container, false)

        viewPager = view.findViewById(R.id.viewPager)




        roomtypesList = arguments?.getSerializable(KEY_GET_FLAT_INTO_ADAPTER) as List<Roomtype>

        if (roomtypesList.isNotEmpty()) {
            viewPager.adapter = ApartmentsPagerAdapter(this, roomtypesList)
        }
        val springDotsIndicator = view.findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)

        springDotsIndicator.attachTo(viewPager)
        return view
    }

    // Адаптер для ViewPager
    private inner class ApartmentsPagerAdapter(
        fragment: Fragment,
        private val roomtypesList: List<Roomtype>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return roomtypesList.size
        }

        override fun createFragment(position: Int): Fragment {
            // отображения информации о квартире
            val apartmentFragment = FlatFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_GET_FLAT_INTO_FLATFRAGMENT, roomtypesList[position])
            apartmentFragment.arguments = bundle
            return apartmentFragment
        }
    }

    companion object{
        const val KEY_GET_FLAT_INTO_ADAPTER = "GET_FOR_ADAPTER"
    }
}


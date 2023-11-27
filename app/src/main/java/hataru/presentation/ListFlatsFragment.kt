package hataru.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.databinding.FragmentListFlatsBinding
import com.example.listrooms.presentation.ApartmentListAdapter
import com.example.listrooms.presentation.ListFlatsViewModel


class ListFlatsFragment : Fragment() {

    private lateinit var viewModel: ListFlatsViewModel
    private lateinit var apartmentListAdapter: ApartmentListAdapter
    private var apartmentContainer: FragmentContainerView? = null
    private lateinit var binding : FragmentListFlatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListFlatsBinding.inflate(layoutInflater)

        //apartmentContainer = binding.rvApartmentList
        setupRecyclerView()
        viewModel = ViewModelProvider(activity as AppCompatActivity)[ListFlatsViewModel::class.java]
        viewModel.apartmentList.observe(activity as AppCompatActivity) {
            apartmentListAdapter.submitList(it)
        }
        return binding.root
    }



    private fun isOnePaneMode(): Boolean {
        return apartmentContainer == null
    }

//    private fun launchFragment(fragment: Fragment) {
//        supportFragmentManager.popBackStack()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.apartment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }

    private fun setupRecyclerView() {
        val rvApartmentList = binding.rvApartmentList
        with(rvApartmentList) {
            apartmentListAdapter = ApartmentListAdapter()
            adapter = apartmentListAdapter
            rvApartmentList.recycledViewPool.setMaxRecycledViews(
                ApartmentListAdapter.VIEW_TYPE_LIKED,
                ApartmentListAdapter.MAX_POOL_SIZE
            )
            rvApartmentList.recycledViewPool.setMaxRecycledViews(
                ApartmentListAdapter.VIEW_TYPE_NOLIKED,
                ApartmentListAdapter.MAX_POOL_SIZE
            )
        }

        setupLikeButtonClickListener()
       // setupApartmentClickListener()
    }

//    private fun setupApartmentClickListener() {
//        apartmentListAdapter.onApartmentClickListener = {
//            if (isOnePaneMode()) {
//                val intent = ApartmentActivity.newIntent(activity as AppCompatActivity, it.id)
//                startActivity(intent)
//            } else {
//                launchFragment(ApartmentFragment.newInstanceItem(it.id))
//            }
//        }
//    }

    private fun setupLikeButtonClickListener() {
        apartmentListAdapter.onLikeButtonClickListener = {
            viewModel.changeLikedStage(it)
        }
    }
}

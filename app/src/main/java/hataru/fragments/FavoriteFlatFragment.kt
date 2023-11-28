package hataru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import hataru.presentation.ApartmentActivity
import hataru.presentation.ApartmentFragment
import hataru.presentation.ApartmentListAdapter
import hataru.presentation.FavoriteFlatViewModel

class FavoriteFlatFragment : Fragment() {

    private lateinit var viewModel: FavoriteFlatViewModel
    private lateinit var apartmentListAdapter: ApartmentListAdapter
    private var apartmentContainer: FragmentContainerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_flats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apartmentContainer = view.findViewById(R.id.apartment_container)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[FavoriteFlatViewModel::class.java]
        viewModel.apartmentList.observe(viewLifecycleOwner) {
            apartmentListAdapter.submitList(it)
        }
    }

    private fun isOnePaneMode(): Boolean {
        val apartmentContainer = activity?.findViewById<FragmentContainerView>(R.id.apartment_container)
        return apartmentContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction()
            .replace(R.id.apartment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        val rvApartmentList = view?.findViewById<RecyclerView>(R.id.rv_apartment_list)
        with(rvApartmentList) {
            apartmentListAdapter = ApartmentListAdapter()
            this?.adapter = apartmentListAdapter
            rvApartmentList?.recycledViewPool?.setMaxRecycledViews(
                ApartmentListAdapter.VIEW_TYPE_LIKED,
                ApartmentListAdapter.MAX_POOL_SIZE
            )
            rvApartmentList?.recycledViewPool?.setMaxRecycledViews(
                ApartmentListAdapter.VIEW_TYPE_NOLIKED,
                ApartmentListAdapter.MAX_POOL_SIZE
            )
        }

        setupLikeButtonClickListener()
        setupApartmentClickListener()
    }

    private fun setupApartmentClickListener() {
        apartmentListAdapter.onApartmentClickListener = {
            if (isOnePaneMode()) {
                val intent = ApartmentActivity.newIntent(requireContext(), it.id)
                startActivity(intent)
            } else {
                launchFragment(ApartmentFragment.newInstanceItem(it.id))
            }
        }
    }

    private fun setupLikeButtonClickListener() {
        apartmentListAdapter.onLikeButtonClickListener = {
            viewModel.changeLikedStage(it)
        }
    }
}


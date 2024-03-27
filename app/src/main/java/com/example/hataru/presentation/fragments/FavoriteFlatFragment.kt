package com.example.hataru.presentation.fragments



import FavoriteFlatViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.presentation.adapter.RoomtypeAdapter
import com.example.hataru.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFlatFragment : Fragment() {

    private val viewModel by viewModel<FavoriteFlatViewModel>()
    private lateinit var apartmentListAdapter: RoomtypeAdapter
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


        viewModel.favoriteFlats.observe(viewLifecycleOwner) { favoriteFlats ->
            Log.d("FavoriteFlatFragment", "Favorite flats observed: $favoriteFlats")
            // В этом блоке вы можете обновить ваш адаптер с новыми данными favoriteFlats
            // Например:
            apartmentListAdapter.submitList(favoriteFlats)
            showToast(favoriteFlats.size.toString())
        }







    }



    private fun isOnePaneMode(): Boolean {
        val apartmentContainer =
            activity?.findViewById<FragmentContainerView>(R.id.apartment_container)
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
            apartmentListAdapter = RoomtypeAdapter()
            this?.adapter = apartmentListAdapter
        }

        setupLikeButtonClickListener()
        setupApartmentClickListener()
    }

    private fun setupLikeButtonClickListener() {
        //TODO

    }
    private fun setupApartmentClickListener() {
//        apartmentListAdapter.onApartmentClickListener = {
//            if (isOnePaneMode()) {
//                val intent = ApartmentActivity.newIntent(requireContext(), it.id)
//                startActivity(intent)
//            } else {
//                launchFragment(ApartmentFragment.newInstanceItem(it.id))
//            }
//        }
//    }

//    private fun setupLikeButtonClickListener() {
//        apartmentListAdapter.onLikeButtonClickListener = {
//            viewModel.changeLikedStage(it)
//        }
    }
}



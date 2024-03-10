package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.domain.entity.Roomtype
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.example.hataru.presentation.ApartmentActivity

import com.example.hataru.presentation.adapter.RoomtypeAdapter
import com.example.hataru.presentation.viewModels.ListFlatsViewModel
import com.example.hataru.presentation.viewModels.MapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFlatsFragment : Fragment() {

    private val viewModel by viewModel<ListFlatsViewModel>()

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






        viewModel.combinedData.observe(viewLifecycleOwner) { (roomtypes, roomxList) ->
            roomtypes?.let { roomtypes ->
                roomxList?.let { roomxList ->
                    val roomtypeWithPhotosList = roomtypes.map { roomtype ->
                        val matchingPhoto = roomxList.firstOrNull { roomx ->
                            roomx.name == roomtype.name
                        }?.photos ?: emptyList()
                        RoomtypeWithPhotos(roomtype, matchingPhoto)
                    }
                    apartmentListAdapter.submitList(roomtypeWithPhotosList)
                }
            }
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
            apartmentListAdapter = RoomtypeAdapter()
            this?.adapter = apartmentListAdapter


        }

//        setupLikeButtonClickListener()
        setupApartmentClickListener()
    }

    private fun setupApartmentClickListener() {
        apartmentListAdapter.onApartmentClickListener = {

            val args = Bundle()
            args.putParcelable(FlatFragment.KEY_GET_FLAT_INTO_FLATFRAGMENT, it.roomtype as Parcelable)

            findNavController().navigate(R.id.flatFragment,args)
            
        }
//        apartmentListAdapter.onApartmentClickListener = {
//            if (isOnePaneMode()) {
//                val intent = ApartmentActivity.newIntent(requireContext(), it.id)
//                startActivity(intent)
//            } else {
//                launchFragment(ApartmentFragment.newInstanceItem(it.id))
//            }
//        }
    }
//
//    private fun setupLikeButtonClickListener() {
//        apartmentListAdapter.onLikeButtonClickListener = {
//            viewModel.changeLikedStage(it)
//        }
//    }
}

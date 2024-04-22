package com.example.hataru.presentation.fragments

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.domain.entity.RoomtypeWithPhotos
import com.example.hataru.presentation.adapter.RoomtypeAdapter
import com.example.hataru.presentation.viewModels.ListFlatsViewModel
import com.example.hataru.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFlatsFragment : Fragment() {

    private val viewModel by viewModel<ListFlatsViewModel>()

    private var roomtypeWithPhotosList: List<RoomtypeWithPhotos> = emptyList()

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
                    roomtypeWithPhotosList = roomtypes.map { roomtype ->
                        val matchingPhoto = roomxList.firstOrNull { roomx ->
                            roomx.name == roomtype.name
                        }?.photos ?: emptyList()
                        RoomtypeWithPhotos(roomtype, matchingPhoto)
                    }
                    setupRecyclerView()
                    apartmentListAdapter.submitList(roomtypeWithPhotosList)
                }
            }
        }


        view.findViewById<ImageButton>(R.id.imageView7)?.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        val editTextSearch = view.findViewById<EditText>(R.id.editText_search)
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Вызывается перед изменением текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch()
            }

            override fun afterTextChanged(s: Editable?) {
                // Вызывается после изменения текста
                performSearch()
            }
        })

    }

    override fun onPause() {
        super.onPause()
        clearSearchEditTextAndHideNoResults()
    }

    override fun onResume() {
        super.onResume()
        clearSearchEditTextAndHideNoResults()
    }

    private fun clearSearchEditTextAndHideNoResults() {
        val editTextSearch = view?.findViewById<EditText>(R.id.editText_search)
        editTextSearch?.setText("") // Очищаем строку поиска
        view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.GONE // Скрываем надпись "Ничего не найдено"
    }

    private fun performSearch() {
        val query = view?.findViewById<EditText>(R.id.editText_search)?.text.toString().trim()
        val filteredList = roomtypeWithPhotosList.filter { roomtypeWithPhotos ->
            apartmentListAdapter.mdesc[roomtypeWithPhotos.roomtype.id]!!.contains(query, ignoreCase = true)
        }
        if (filteredList.isEmpty()) {
            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.VISIBLE
        } else {
            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.GONE
        }
        apartmentListAdapter.filter(query)
    }

//    private fun isOnePaneMode(): Boolean {
//        val apartmentContainer = activity?.findViewById<FragmentContainerView>(R.id.apartment_container)
//        return apartmentContainer == null
//    }
//
//    private fun launchFragment(fragment: Fragment) {
//        parentFragmentManager.popBackStack()
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.apartment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }

    private fun setupRecyclerView() {
        val rvApartmentList = view?.findViewById<RecyclerView>(R.id.rv_apartment_list)
        with(rvApartmentList) {
            apartmentListAdapter = RoomtypeAdapter(roomtypeWithPhotosList)
            this?.adapter = apartmentListAdapter


        }

        setupLikeButtonClickListener()
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

    private fun setupLikeButtonClickListener() {
        apartmentListAdapter.onLikeButtonClickListener = { flat ->
            viewModel.changeLikedStage(flat)
            showToast("Квартира добавлена в избранные!")
        }
    }
}

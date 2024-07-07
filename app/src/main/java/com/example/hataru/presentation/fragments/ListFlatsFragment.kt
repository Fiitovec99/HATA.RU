package com.example.hataru.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            if (roomtypes != null && roomxList != null) {
                roomtypeWithPhotosList = roomtypes.map { roomtype ->
                    val matchingRoomXList = roomxList.filter { roomx ->
                        roomx.name == roomtype.name
                    }
                    RoomtypeWithPhotos(roomtype, matchingRoomXList)
                }
                Log.d("ListFlatsFragment", "roomtypeWithPhotosList заполнен с ${roomtypeWithPhotosList.size} элементами.")
                apartmentListAdapter.submitList(roomtypeWithPhotosList)
            }
        }



        view.findViewById<ImageButton>(R.id.imageView7)?.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

//        view.findViewById<ImageView>(R.id.imageButton_filter)?.setOnClickListener {
//            showFilterDialog()
//        }

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
        view?.findViewById<TextView>(R.id.text_no_results)?.visibility =
            View.GONE // Скрываем надпись "Ничего не найдено"
    }

    private fun performSearch() {
        val editTextSearch = view?.findViewById<EditText>(R.id.editText_search)
        val query = editTextSearch?.text.toString().trim()

        // Проверка наличия данных в roomtypeWithPhotosList
        if (roomtypeWithPhotosList.isEmpty()) {
            Log.d("performSearch", "Список roomtypeWithPhotosList пуст.")
            return
        }

        // Проверка наличия данных в строке поиска
        if (query.isEmpty()) {
            Log.d("performSearch", "Поисковый запрос пуст, сбрасываем список.")
            apartmentListAdapter.submitList(roomtypeWithPhotosList)
            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.GONE
            return
        }

        // Фильтруем список по названию
        val filteredList = roomtypeWithPhotosList.filter { roomtypeWithPhotos ->
            roomtypeWithPhotos.roomtype.name!!.contains(query, ignoreCase = true)
        }

        // Логгирование результатов фильтрации
        Log.d("performSearch", "Фильтрованный список содержит ${filteredList.size} элементов.")

        // Показываем или скрываем текст "Ничего не найдено"
        if (filteredList.isEmpty()) {
            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.VISIBLE
        } else {
            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.GONE
        }

        // Обновляем адаптер с отфильтрованным списком
        apartmentListAdapter.submitList(filteredList)
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

//    private fun showFilterDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_filter, null)
//        val minPriceEditText = dialogView.findViewById<EditText>(R.id.editText_min_price)
//        val maxPriceEditText = dialogView.findViewById<EditText>(R.id.editText_max_price)
//        val minAreaEditText = dialogView.findViewById<EditText>(R.id.editText_min_area)
//        val maxAreaEditText = dialogView.findViewById<EditText>(R.id.editText_max_area)
//
//        AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setTitle("Фильтр квартир")
//            .setPositiveButton("Применить") { dialog, _ ->
//                val minPrice = minPriceEditText.text.toString().toIntOrNull() ?: Int.MIN_VALUE
//                val maxPrice = maxPriceEditText.text.toString().toIntOrNull() ?: Int.MAX_VALUE
//                val minArea = minAreaEditText.text.toString().toIntOrNull() ?: Int.MIN_VALUE
//                val maxArea = maxAreaEditText.text.toString().toIntOrNull() ?: Int.MAX_VALUE
//
//                performFilter(minPrice, maxPrice, minArea, maxArea)
//                dialog.dismiss()
//            }
//            .setNegativeButton("Отмена") { dialog, _ ->
//                dialog.cancel()
//            }
//            .show()
//    }
//
//    private fun performFilter(minPrice: Int, maxPrice: Int, minArea: Int, maxArea: Int) {
//        val filteredList = roomtypeWithPhotosList.filter { roomtypeWithPhotos ->
//            val price = roomtypeWithPhotos.roomtype.price.toInt()
//            val area = apartmentListAdapter.marea[roomtypeWithPhotos.roomtype.id]?.toInt()
//            price in minPrice..maxPrice && area in minArea..maxArea
//        }
//        if (filteredList.isEmpty()) {
//            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.VISIBLE
//        } else {
//            view?.findViewById<TextView>(R.id.text_no_results)?.visibility = View.GONE
//        }
//        apartmentListAdapter.submitList(filteredList)
//    }

    private fun setupRecyclerView() {
        val rvApartmentList = view?.findViewById<RecyclerView>(R.id.rv_apartment_list)
        with(rvApartmentList) {
            apartmentListAdapter = RoomtypeAdapter(roomtypeWithPhotosList, requireContext())
            this?.adapter = apartmentListAdapter
            setupLikeButtonClickListener() // Добавьте эту строку
        }

        setupApartmentClickListener()
    }


    private fun setupApartmentClickListener() {
        apartmentListAdapter.onApartmentClickListener = {

            val args = Bundle()
            args.putParcelable(
                FlatFragment.KEY_GET_FLAT_INTO_FLATFRAGMENT,
                it.roomtype as Parcelable
            )

            findNavController().navigate(R.id.flatFragment, args)

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
        }
    }
}

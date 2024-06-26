package com.example.hataru.presentation.fragments


import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hataru.R
import com.example.hataru.SharedPreferenceManger
import com.example.hataru.databinding.FragmentMenuBinding
import com.example.hataru.presentation.activities.OnboardingActivity
import com.example.hataru.presentation.adapter.FAQAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment() {


    private lateinit var binding: FragmentMenuBinding
    private val themeTitleList = arrayOf("Светлая", "Тёмная", "Системная")

    private val listDataHeader = mutableListOf<String>(
        "Во сколько заезд и выезд?",
        "Что такое бесконтактное заселение?",
        "Что нужно взять с собой для заселения?",
        "Как часто убирают апартаменты?",
        "Можно курить в апартаментах?",
        "Можно заселиться с детьми или животными?",
        "В апартаментах есть посуда и приборы?"
    )

    private val listHashMap = hashMapOf<String, List<String>>(
        "Во сколько заезд и выезд?" to listOf(
            "Обычно время заезда с 14:00. Но если квартира убрана и проверена раньше 14:00, то мы с радостью заселим вас сразу после проверки без доплаты.\n" +
                    "\n" +
                    "Время выезда в 12:00. Однако, есть возможность почасового продления."
        ),
        "Что такое бесконтактное заселение?" to listOf("Доступ в апартаменты  осуществляется с помощью кодовых замков. Вам будет предоставлен код, который действует в период Вашего проживания. Для заселения в апартаменты не потребуется встречаться с менеджером."),
        "Что нужно взять с собой для заселения?" to listOf("Для заезда необходимо иметь при себе паспорт или водительское удостоверение и залог, размер которого укажет менеджер."),
        "Как часто убирают апартаменты?" to listOf("Уборка в апартаментах осуществляется ежедневно, с применением профессиональных моющих средств."),
        "Можно курить в апартаментах?" to listOf("В апартаментах курение строго запрещено. Взимается Штраф в размере залога."),
        "Можно заселиться с детьми или животными?" to listOf("Конечно, в апартаментах есть все необходимое для комфортного проживания не только взрослых, но и детей.\n" +
                "\n" +
                "Заселиться с питомцем тоже можно, но обязательно предупредите об этом менеджера."),
        "В апартаментах есть посуда и приборы?" to listOf("Кухни в наших апартаментах оборудованы всей необходимой мебелью, техникой, а также посудой для приготовления и приёма пищи.")
    )




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.LinkToTheWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hataru.ru"))
            startActivity(intent)
        }
        binding.textViewOpenWhatsUp.setOnClickListener {
            val phoneNumber = "+79959890049"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("whatsapp://send?phone=$phoneNumber")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, "Приложение не установлено", Toast.LENGTH_SHORT).show()
            }
        }

        binding.exitAccount.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, OnboardingActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        // Инициализация ExpandableListView
        val expandableListView = binding.expandableListView

        // Создание экземпляра адаптера и установка его для ExpandableListView
        val listAdapter = FAQAdapter(requireContext(), listDataHeader, listHashMap)
        expandableListView.setAdapter(listAdapter)


        ////////////////////////////////////////работа с темой
        val sharedPreferenceManger = SharedPreferenceManger(requireContext())
        var checkedTheme = sharedPreferenceManger.theme

        val themeDialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.CustomDialogTheme
        )
            .setTitle("Тема приложения")
            .setPositiveButton("Ok") { _, _ ->
                sharedPreferenceManger.theme = checkedTheme
                AppCompatDelegate.setDefaultNightMode(sharedPreferenceManger.themeFlag[checkedTheme])
            }
            .setSingleChoiceItems(themeTitleList, checkedTheme) { _, which ->
                checkedTheme = which
            }
            .setCancelable(false)


        binding.changeThemeBtn.setOnClickListener {
            themeDialog.show()
        }
        //////////////////////////////////////////////


    }

}

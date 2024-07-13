package com.example.hataru.presentation.fragments

import FAQAdapter
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hataru.R
import com.example.hataru.SharedPreferenceManger
import com.example.hataru.databinding.FragmentMenuBinding
import com.example.hataru.domain.entity.FAQItem
import com.example.hataru.presentation.activities.OnboardingActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private val themeTitleList = arrayOf("Светлая", "Тёмная", "Системная")

//    private val faqList = listOf(
//        "Во сколько заезд и выезд?" to "Обычно время заезда с 14:00. Но если квартира убрана и проверена раньше 14:00, то мы с радостью заселим вас сразу после проверки без доплаты.\n\nВремя выезда в 12:00. Однако, есть возможность почасового продления.",
//        "Что такое бесконтактное заселение?" to "Доступ в апартаменты осуществляется с помощью кодовых замков. Вам будет предоставлен код, который действует в период Вашего проживания. Для заселения в апартаменты не потребуется встречаться с менеджером.",
//        "Что нужно взять с собой для заселения?" to "Для заезда необходимо иметь при себе паспорт или водительское удостоверение и залог, размер которого укажет менеджер.",
//        "Как часто убирают апартаменты?" to "Уборка в апартаментах осуществляется ежедневно, с применением профессиональных моющих средств.",
//        "Можно курить в апартаментах?" to "В апартаментах курение строго запрещено. Взимается Штраф в размере залога.",
//        "Можно заселиться с детьми или животными?" to "Конечно, в апартаментах есть все необходимое для комфортного проживания не только взрослых, но и детей.\n\nЗаселиться с питомцем тоже можно, но обязательно предупредите об этом менеджера.",
//        "В апартаментах есть посуда и приборы?" to "Кухни в наших апартаментах оборудованы всей необходимой мебелью, техникой, а также посудой для приготовления и приёма пищи."
//    )
    val faqList = listOf(
        FAQItem("Во сколько заезд и выезд?", "Обычно время заезда с 14:00. Но если квартира убрана и проверена раньше 14:00, то мы с радостью заселим вас сразу после проверки без доплаты."),
        FAQItem("Что такое бесконтактное заселение?", "Доступ в апартаменты осуществляется с помощью кодовых замков. Вам будет предоставлен код, который действует в период Вашего проживания. Для заселения в апартаменты не потребуется встречаться с менеджером."),
        FAQItem("Что нужно взять с собой для заселения?", "Для заезда необходимо иметь при себе паспорт или водительское удостоверение и залог, размер которого укажет менеджер."),
        FAQItem("Как часто убирают апартаменты?", "Уборка в апартаментах осуществляется ежедневно, с применением профессиональных моющих средств."),
        FAQItem("Можно курить в апартаментах?", "В апартаментах курение строго запрещено. Взимается Штраф в размере залога."),
        FAQItem("Можно заселиться с детьми или животными?", "Конечно, в апартаментах есть все необходимое для комфортного проживания не только взрослых, но и детей.\n\nЗаселиться с питомцем тоже можно, но обязательно предупредите об этом менеджера."),
        FAQItem("В апартаментах есть посуда и приборы?", "Кухни в наших апартаментах оборудованы всей необходимой мебелью, техникой, а также посудой для приготовления и приёма пищи.")
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

        // Инициализация RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FAQAdapter(faqList)
        }

        // Работа с темой
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
    }
}

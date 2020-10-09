package com.idrok.a3003.ui.languageFragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.data.LANGUAGE
import com.idrok.a3003.data.LanguageChanger
import kotlinx.android.synthetic.main.fragment_language.view.*


class LanguageFragment : Fragment(R.layout.fragment_language) {
    private lateinit var rootView: View
    private lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        (requireActivity() as AppCompatActivity).setSupportActionBar(rootView.toolbar_lang)
        setViews()
        setOnClicks()
    }

    private fun setOnClicks() {

        rootView.apply {
            toolbar_lang.setNavigationOnClickListener {
                findNavController().navigate(R.id.mainFragment)
            }
            tv_lang_ru.setOnClickListener {
                saveLang("ru")
            }
            tv_lang_cyrl.setOnClickListener {
                saveLang("cyrl")
            }
            tv_lang_uz.setOnClickListener {
                saveLang("uz")
            }
        }
    }

    private fun saveLang(lang: String) {
        prefs.edit()
            .putString(LANGUAGE, lang)
            .apply()
        LanguageChanger(requireContext()).setLanguage()
        findNavController().navigate(R.id.mainFragment)
    }

    private fun setViews() {
        val lang = prefs.getString(LANGUAGE, "ru")
        rootView.apply {
            tv_lang_uz.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tv_lang_cyrl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            tv_lang_ru.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                when (lang) {
                    "uz" -> {
                        tv_lang_uz.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_check,
                            0
                        )
                        return
                    }
                    "cyrl" -> {
                        tv_lang_cyrl.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_check,
                            0
                        )
                        return
                    }
                    "ru" -> {
                        tv_lang_ru.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_check,
                            0
                        )
                        return
                    }
                }
        }
    }
}
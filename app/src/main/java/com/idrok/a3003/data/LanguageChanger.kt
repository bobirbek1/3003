package com.idrok.a3003.data

import android.content.Context
import com.idrok.a3003.PREFERENCE_KEY
import java.util.*

const val LANGUAGE = "language"
class LanguageChanger(private val context: Context){

    fun setLanguage(){
        val locale = getLocale()
        val res = context.resources
        val conf = res.configuration
        if (conf.locale != locale){
            conf.setLocale(locale)
            res.updateConfiguration(conf,res.displayMetrics)
        }
    }

    private fun getLocale(): Locale {
        val prefs = context.getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        return when(prefs.getString(LANGUAGE,"ru")){
            "ru" -> {
                Locale("ru","RU")
            }
            "uz" -> {
                Locale("uz","UZ")
            }
            "cyrl" -> {
                Locale("ru","KZ")
            }
            else -> {
                Locale("ru","RU")
            }
        }
    }

}
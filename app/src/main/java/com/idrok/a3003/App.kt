package com.idrok.a3003

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.idrok.a3003.data.LANGUAGE
import java.util.*

const val PREFERENCE_KEY = "preference_key"
class App : Application(){

    override fun onCreate() {
        getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocale()
    }

    private fun setLocale() {
        val res = resources
        val conf = res.configuration
        val locale = getLocale()
        if (conf.locale != locale){
            conf.setLocale(locale)
            res.updateConfiguration(conf, baseContext.resources.displayMetrics)
        }
    }

    private fun getLocale(): Locale {
        val prefs = getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
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
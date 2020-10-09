package com.idrok.a3003.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.idrok.a3003.R
import com.idrok.a3003.data.LanguageChanger


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var lastOnBackPressed: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setLang()
    }

    private fun setLang() {
        val languageChanger = LanguageChanger(this)
        languageChanger.setLanguage()
    }


    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.mainFragment -> {
                if (doubleClicked()) {
                    finishAffinity()
                }
                return
            }
            else -> {
                super.onBackPressed()
            }
        }

    }

    private fun doubleClicked(): Boolean {
        return if (lastOnBackPressed + 2000 >= System.currentTimeMillis()) {
            true
        } else {
            Toast.makeText(this, "Chiqish uchun yana bir marta bosing", Toast.LENGTH_SHORT).show()
            lastOnBackPressed = System.currentTimeMillis()
            false
        }
    }
}
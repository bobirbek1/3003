package com.idrok.a3003.ui.customizeSearch

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.idrok.a3003.R

class CustomizeSearch(private val context: Activity) {

    fun customizeSearch(menu: Menu,searchView: SearchView,callback:((String?,String?) -> Unit)) {
        val searchManager =
            context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(context.componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE

        cusomizeSearchView(searchView)

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
              callback.invoke(query,null)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                callback.invoke(null,query)
                return false
            }
        })
    }

    private fun cusomizeSearchView(searchView: SearchView) {
        val searchButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchButton.setColorFilter(Color.WHITE)
        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(Color.BLACK)

        val searchPlate = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundResource(R.drawable.search_bg_rounded)

        val closeButton =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeButton.setColorFilter(
            ContextCompat.getColor(context, R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )


    }

}
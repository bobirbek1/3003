package com.idrok.a3003.ui.dataFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.data.DocDopol
import com.idrok.a3003.data.GetData
import com.idrok.a3003.data.SearchState
import com.idrok.a3003.data.LANGUAGE
import com.idrok.a3003.ui.bookmark.TITLE
import com.idrok.a3003.ui.customizeSearch.CustomizeSearch
import com.idrok.a3003.ui.listFragment.DATA_SECTION
import com.idrok.a3003.utils.toast
import kotlinx.android.synthetic.main.fragment_data.view.*
import java.util.*


const val BOOKMARK_LIST = "position_list"

class DataFragment : Fragment(R.layout.fragment_data) {

    private lateinit var rootView: View
    private lateinit var getData: GetData
    private lateinit var prefs: SharedPreferences
    private lateinit var docDopol: DocDopol

    private var pdfName: String = ""
    private var section = -1
    private var listBookmarks = arrayListOf<Int>()
    private var listPdfName = arrayListOf<String>()
    private lateinit var timer: Timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setViews()
        showAd()

    }

    private fun showAd() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                rootView.motion_layout.transitionToEnd()
            }
        }, 10000)
        rootView.iv_data_close.setOnClickListener {
            rootView.motion_layout.transitionToStart()
        }
    }

    private fun setToolbarTitle() {
        val title =
            requireArguments().getString(TITLE, requireContext().getString(R.string.app_name))
        if (!title.isNullOrEmpty()) {
            rootView.toolbar_data.title = title
        } else {
            rootView.toolbar_data.title = requireContext().getString(R.string.app_name)
        }
    }

    private fun getData() {
        section = requireArguments().getInt(DATA_SECTION, -1)
        when (prefs.getString(LANGUAGE, "ru")) {
            "uz" -> {
                listPdfName = getData.getPdfNameListUz()
                pdfName = listPdfName[section]
                if (pdfName.isNotEmpty())
                    setPdfWithSearch(pdfName)
                else
                    requireContext().toast("No PDF document found")
            }
            "cyrl" -> {
                listPdfName = getData.getPdfNameListCyrl()
                pdfName = listPdfName[section]
                if (pdfName.isNotEmpty())
                    setPdfWithSearch(pdfName)
                else
                    requireContext().toast("No PDF document found")
            }
            "ru" -> {
                listPdfName = getData.getPdfNameListRu()
                pdfName = listPdfName[section]
                if (pdfName.isNotEmpty())
                    setPdfWithSearch(pdfName)
                else
                    requireContext().toast("No PDF document found")

            }
        }
        checkBookmarks()
    }

    private fun checkBookmarks(): Boolean {
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        val jsonString = prefs.getString(BOOKMARK_LIST, "")
        listBookmarks = Gson().fromJson(jsonString, type) ?: arrayListOf()

        val position = listPdfName.binarySearch(pdfName)
        Log.d("DataFragment", "list1:$listBookmarks")
        return listBookmarks.contains(position)
    }

    private fun setViews() {
        val toolbar = rootView.toolbar_data
        timer = Timer()
        rootView.cv_search.visibility = View.GONE
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        setHasOptionsMenu(true)
        getData = GetData(requireContext())
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setToolbarTitle()
        setOnClicks()
        getData()
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.data_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        val searchView = menu.findItem(R.id.data_search).actionView as SearchView
        CustomizeSearch(requireActivity()).customizeSearch(menu,searchView) { submitted, _ ->
            if (!submitted.isNullOrEmpty()){
            if (docDopol.searchState == SearchState.SearchStart) {
                docDopol.stop()
            }
            docDopol.search(submitted)
                rootView.cv_search.visibility = View.VISIBLE
            }
        }
        searchView.setOnCloseListener {
            docDopol.reset()
            docDopol.open()
            rootView.cv_search.visibility = View.GONE
            searchView.onActionViewCollapsed()
            return@setOnCloseListener true
        }
        if (checkBookmarks()) {
            menu.findItem(R.id.data_bookmark).setIcon(R.drawable.ic_bookmark_filled)
        } else {
            menu.findItem(R.id.data_bookmark).setIcon(R.drawable.ic_bookmark_border)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val position = listPdfName.binarySearch(pdfName)
        return when (item.itemId) {
            R.id.data_share -> {
                true
            }
            R.id.data_bookmark -> {
                if (checkBookmarks()) {
                    item.setIcon(R.drawable.ic_bookmark_border)
                    listBookmarks.remove(position)
                    requireContext().toast("document removed from bookmarks")
                    Log.d("DataFragment", "list2:$listBookmarks")
                    saveBookmarks()
                } else {
                    item.setIcon(R.drawable.ic_bookmark_filled)
                    listBookmarks.add(position)
                    requireContext().toast("document add to bookmarks")
                    Log.d("DataFragment", "list3:$listBookmarks")
                    saveBookmarks()
                }
                true
            }
            else -> {
                false
            }
        }
    }

    private fun setPdfWithSearch(pdfName: String) {
        val pdfReader = rootView.pdf_data
        docDopol = DocDopol.getInstance(requireContext(), rootView.pdf_data, pdfName)
        Log.d("PdfFragment", "setPdf:$pdfReader")
        docDopol.open()
    }

    override fun onPause() {
        super.onPause()
        saveBookmarks()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        docDopol.Destroy()
    }

    private fun saveBookmarks() {
        listBookmarks.sortWith { lhs, rhs ->
            lhs.compareTo(rhs)
        }
        val jsonString = Gson().toJson(listBookmarks)
        prefs.edit()
            .putString(BOOKMARK_LIST, jsonString)
            .apply()
    }

    private fun setOnClicks() {
//        rootView.tv_home.setOnClickListener {
//
//        }
//        rootView.tv_bookmark.setOnClickListener { }
    }

//    private fun getNameFromArguments(): String {
//        return requireArguments().getString(DATA_NAME, "")
//    }
}

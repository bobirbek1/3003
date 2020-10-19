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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.data.readFromText.ReadFromText
import com.idrok.a3003.ui.bookmark.TITLE
import com.idrok.a3003.ui.dataFragment.adapter.ViewPagerAdapter
import com.idrok.a3003.ui.listFragment.DOC_ITEM_POS
import com.idrok.a3003.utils.toast
import kotlinx.android.synthetic.main.fragment_data.view.*
import kotlinx.android.synthetic.main.tab_item.view.*
import java.util.*


const val BOOKMARK_LIST = "position_list"

class DataFragment : Fragment(R.layout.fragment_data) {

    private lateinit var rootView: View
    private lateinit var getData: GetData
    private lateinit var prefs: SharedPreferences
//    private lateinit var docDopol: DocDopol

    private var pdfName: String = ""
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

    private fun getData(): Int {
        return requireArguments().getInt(DOC_ITEM_POS, 0)
//        val listData = ReadFromText.getData(position)
//        section = requireArguments().getInt(DOC_SECTION, -1)
//        when (prefs.getString(LANGUAGE, "ru")) {
//            "uz" -> {
//                listPdfName = getData.getPdfNameListUz()
//                pdfName = listPdfName[section]
//                if (pdfName.isNotEmpty())
//                    setPdfWithSearch(pdfName)
//                else
//                    requireContext().toast("No PDF document found")
//            }
//            "cyrl" -> {
//                listPdfName = getData.getPdfNameListCyrl()
//                pdfName = listPdfName[section]
//                if (pdfName.isNotEmpty())
//                    setPdfWithSearch(pdfName)
//                else
//                    requireContext().toast("No PDF document found")
//            }
//            "ru" -> {
//                listPdfName = getData.getPdfNameListRu()
//                pdfName = listPdfName[section]
//                if (pdfName.isNotEmpty())
//                    setPdfWithSearch(pdfName)
//                else
//                    requireContext().toast("No PDF document found")
//            }
//        }
//        checkBookmarks()
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
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        setHasOptionsMenu(true)
        getData = GetData(requireContext())
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setToolbarTitle()
        setOnClicks()
        setViewPagerWithTab()
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setViewPagerWithTab() {
        val vp = rootView.vp_data
        val tabLayout = rootView.tab_data
        val pos = getData()
        val adapter = ViewPagerAdapter(getChildCount(pos), pos, this)
        vp.adapter = adapter

        TabLayoutMediator(tabLayout, vp) { tab, position ->
            tab.setCustomView(R.layout.tab_item)
            (tab.customView as View).tv_indicator.text = (position + 1).toString()
        }.attach()
        val tab = tabLayout.getTabAt(0)?.customView as View
        tab.tv_indicator.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorWhite
            )
        )
        tab.cv_data_indicator.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val view = tab?.customView as View
                view.tv_indicator.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.cv_data_indicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val view = tab?.customView as View
                view.tv_indicator.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                view.cv_data_indicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun getChildCount(pos: Int): Int {
        val count = ReadFromText.getDataChild(pos).size
        return if (count == 0) {
            1
        } else {
            count
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.data_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

//        val searchView = menu.findItem(R.id.data_search).actionView as SearchView
//        CustomizeSearch(requireActivity()).customizeSearch(menu, searchView) { submitted, _ ->
//            if (!submitted.isNullOrEmpty()) {
//                if (docDopol.searchState == SearchState.SearchStart) {
//                    docDopol.stop()
//                }
//                search(submitted)
//            }
//        }
//        searchView.setOnCloseListener {
//            docDopol.reset()
//            docDopol.open()
//            setNavigateVisibility(false)
//            searchView.onActionViewCollapsed()
//            return@setOnCloseListener true
//        }
        if (checkBookmarks()) {
            menu.findItem(R.id.data_bookmark).setIcon(R.drawable.ic_bookmark_filled)
        } else {
            menu.findItem(R.id.data_bookmark).setIcon(R.drawable.ic_bookmark_border)
        }
    }

//    private fun search(submitted: String) {
//        if (docDopol.search(submitted)) {
//            setNavigation()
//            setNavigateVisibility(true)
//        } else {
//            requireActivity().toast("Search bar is empty")
//        }
//    }


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

//    private fun setPdfWithSearch(pdfName: String) {
//        val pdfReader = rootView.pdf_data
//        docDopol = DocDopol.getInstance(requireContext(), rootView.pdf_data, pdfName)
//        Log.d("PdfFragment", "setPdf:$pdfReader")
//        docDopol.open()
//    }

    override fun onPause() {
        super.onPause()
        saveBookmarks()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
//        docDopol.Destroy()
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

//    private fun setNavigation() {
//        val pdfNavigation: PdfNavigation = docDopol.pdfNavigation
//        val index = pdfNavigation.countAllArrayMapChild
//        val count = pdfNavigation.countArrayMapChild
//        setTextNavigation(index, count)
//        if (index == 1) {
//            val imgNavLeft = rootView.iv_previous
//            imgNavLeft.isEnabled = false
//        } else {
//            val imgNavLeft = rootView.iv_previous
//            imgNavLeft.isEnabled = true
//        }
//        if (index == count) {
//            val imgNavRight = rootView.iv_next
//            imgNavRight.isEnabled = false
//        } else {
//            val imgNavRight = rootView.iv_next
//            imgNavRight.isEnabled = true
//        }
//    }

//    @SuppressLint("SetTextI18n")
//    private fun setTextNavigation(index: Int, count: Int) {
//        rootView.tv_search_count.text = "$index/$count"
//    }

//    private fun setNavigateVisibility(visibility: Boolean) {
//        if (visibility) {
//            rootView.cv_search.visibility = View.VISIBLE
//            rootView.iv_next.setOnClickListener {
//                if (docDopol.nextCollection()) {
//                    setNavigation()
//                }
//            }
//            rootView.iv_previous.setOnClickListener {
//                if (docDopol.preedCollection()) {
//                    setNavigation()
//                }
//            }
//        } else {
//
//            rootView.cv_search.visibility = View.INVISIBLE
//        }
//    }


//    private fun getNameFromArguments(): String {
//        return requireArguments().getString(DATA_NAME, "")
//    }
}

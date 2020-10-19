package com.idrok.a3003.ui.bookmark


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.ui.bookmark.adapter.Adapter
import com.idrok.a3003.ui.dataFragment.BOOKMARK_LIST
import com.idrok.a3003.ui.listFragment.DOC_SECTION
import kotlinx.android.synthetic.main.fragment_bookmark.view.*

const val PDF_NAME = "pdf_name"
const val TITLE = "title"

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private lateinit var rootView: View
    private lateinit var getData: GetData
    private lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setViews()
        setRv()
    }

    private fun setViews() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(rootView.bookmark_toolbar)
        rootView.bookmark_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        getData = GetData(requireContext())
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    private fun setRv() {
        val listTitles = getListTitles()
        val positionList = getPosition()
        Log.d("BookmarkFragment", "setRv: $positionList")
        val listChoosenTitles = arrayListOf<String>()
        for (position in positionList) {
            listChoosenTitles.add(listTitles[position])
        }
        Log.d("BookmarkFragment", "choosenTitles: $listChoosenTitles")
        val adapter = Adapter(listChoosenTitles) { title ->
            val bundle = Bundle()
            bundle.putInt(DOC_SECTION, listTitles.binarySearch(title))
            bundle.putString(TITLE, requireContext().getString(R.string.bookmark))
            findNavController().navigate(R.id.dataFragment, bundle)
        }
        rootView.rv_bookmark.adapter = adapter
        rootView.rv_bookmark.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

    }

    private fun getPosition(): ArrayList<Int> {
        val string = prefs.getString(BOOKMARK_LIST, "")
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(string, type) ?: arrayListOf()
    }

    private fun getListTitles(): ArrayList<String> = getData.getListTitles()

//
//    private fun getListPdfName(): ArrayList<String> {
//        return when (prefs.getString(LANGUAGE, "ru")) {
//            "uz" -> {
//                getData.getPdfNameListUz()
//            }
//            "cyrl" -> {
//                getData.getPdfNameListCyrl()
//            }
//            "ru" -> {
//                getData.getPdfNameListRu()
//            }
//            else -> {
//                getData.getPdfNameListRu()
//            }
//        }
//    }

}
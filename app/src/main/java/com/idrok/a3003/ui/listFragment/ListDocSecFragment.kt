package com.idrok.a3003.ui.listFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.data.LANGUAGE
import com.idrok.a3003.data.readFromText.ReadFromText
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerChildAdapter
import kotlinx.android.synthetic.main.fragment_list_doc_sec.view.*

const val DOC_ITEM_POS = "doc_item_pos"

class ListDocSecFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var readFromText: ReadFromText
    private lateinit var getData: GetData
    private lateinit var prefs: SharedPreferences
    private lateinit var adapter: ViewPagerChildAdapter
    private var list = arrayListOf<String>()

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_list_doc_sec, container, false)
        setViews()

        return rootView
    }

    private fun setViews() {
        getData = GetData(requireContext())
        readFromText = ReadFromText
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        setRv()
        getListData()
    }

    private fun getListData() {
        list = readFromText.getListTitles()
        adapter.setData(list)
    }

//    private fun getLang(section: Int) {
//        val lang = prefs.getString(LANGUAGE, "ru")
//        if (!lang.isNullOrEmpty()) {
//            when (lang) {
//                "uz" -> {
//                    readFromText = ReadFromText.getInstance(
//                        requireContext(),
//                        getData.getTextNameListUz()[section]
//                    )
//                }
//                "cyrl" -> {
//                    readFromText = ReadFromText.getInstance(
//                        requireContext(),
//                        getData.getTextNameListCyrl()[section]
//                    )
//                }
//                "ru" -> {
//                    readFromText = ReadFromText.getInstance(
//                        requireContext(),
//                        getData.getTextNameListRu()[section]
//                    )
//                }
//                else -> {
//                    readFromText = ReadFromText.getInstance(
//                        requireContext(),
//                        getData.getTextNameListRu()[section]
//                    )
//                }
//            }
//        } else {
//            readFromText =
//                ReadFromText.getInstance(requireContext(), getData.getTextNameListRu()[section])
//        }
//    }

    private fun setRv() {
        adapter = ViewPagerChildAdapter(2) { title ->
            Log.d("ListDocSecFragment", "setRv: $title")
            val bundle = Bundle()
            bundle.putInt(DOC_ITEM_POS,list.indexOf(title))
            findNavController().navigate(R.id.dataFragment, bundle)
        }
        rootView.rv_list_doc_sec.adapter = adapter
        rootView.rv_list_doc_sec.addItemDecoration(
            DividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL
            )
        )
    }

}
package com.idrok.a3003.ui.listFragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.data.LANGUAGE
import com.idrok.a3003.data.readFromText.ReadFromText
import com.idrok.a3003.model.ListItems
import com.idrok.a3003.ui.customizeSearch.CustomizeSearch
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerChildAdapter
import kotlinx.android.synthetic.main.fragment_viewpager_child.view.*


const val DOC_SECTION = "data_name"

class ViewPagerChildFragment : Fragment(R.layout.fragment_viewpager_child) {

    private lateinit var rootView: View
    private lateinit var getData: GetData
    private lateinit var adapter: ViewPagerChildAdapter
    private lateinit var listItems:ArrayList<ListItems>
    private  var list = arrayListOf<String>()
    private lateinit var prefs:SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rootView = view
        setViews()
    }

    private fun setViews() {
        getData = GetData(requireContext())
        prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE)
        setRv()
        getArgs()
        seperateTitles()
    }

    private fun seperateTitles() {
        for (title in listItems){
            list.add(title.body)
        }
        updateAdapter(list)

    }

    private fun updateAdapter(list: ArrayList<String>) {
        adapter.setData(list)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
//        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
//        CustomizeSearch(requireActivity()).customizeSearch(menu, searchView) { _, changed ->
//            if (changed != null)
//                adapter.filter.filter(changed)
//        }
    }

    private fun getArgs() {
        val type = requireArguments().getInt("type", 0)
        val segment = requireArguments().getString("segment", "")
        if (type != 0) {
            when (type) {
                1 -> {
                    if (segment.isNotEmpty()) {
                        if (segment == requireContext().getString(R.string.activeSegment1)) {
                            listItems =  getData.getActiveSchetList1()
                            return
                        } else if (segment == requireContext().getString(R.string.activeSegment2)) {
                            listItems =  getData.getActiveSchetList2()
                            return
                        }
                    }
                }
                2 -> {
                    if (segment.isNotEmpty()) {
                        if (segment == requireContext().getString(R.string.passiveSegment1)) {
                            listItems =  getData.getPassiveSchetList1()
                            return
                        } else if (segment == requireContext().getString(R.string.passiveSegment2)) {
                            listItems = getData.getPassiveSchetList2()
                            return
                        }
                    }
                }
                3 -> {
                    listItems =  getData.getTranzitniyeSchetList()
                    return
                }
                4 -> {
                    listItems =  getData.getZabalansoviyeSchet()
                    return
                }
                else -> {
                    listItems = arrayListOf()
                    return
                }
            }
        }
        listItems = arrayListOf()
    }


    private fun setRv() {
        adapter = ViewPagerChildAdapter(1) { title ->
            val position = list.indexOf(title)
            // RecyclerView itemi bosilganda itemni ListDocSecFragmentga yuboramiz
            getLang(position)
            ReadFromText.Title = title
            findNavController().navigate(R.id.listDocSecFragment)
        }

        rootView.rv_list.adapter = adapter

        rootView.rv_list.layoutManager?.scrollToPosition(0)
        rootView.rv_list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getLang(section: Int) {
        val lang = prefs.getString(LANGUAGE, "ru")
        if (!lang.isNullOrEmpty()) {
            when (lang) {
                "uz" -> {
                   ReadFromText.getInstance(
                        requireContext(),
                        getData.getTextNameListUz()[section]
                    )
                }
                "cyrl" -> {
                    ReadFromText.getInstance(
                        requireContext(),
                        getData.getTextNameListCyrl()[section]
                    )
                }
                "ru" -> {
                     ReadFromText.getInstance(
                        requireContext(),
                        getData.getTextNameListRu()[section]
                    )
                }
                else -> {
                    ReadFromText.getInstance(
                        requireContext(),
                        getData.getTextNameListRu()[section]
                    )
                }
            }
        } else {
                ReadFromText.getInstance(requireContext(), getData.getTextNameListRu()[section])
        }
    }


}
package com.idrok.a3003.data.readFromText

import android.content.Context
import android.util.Log
import com.idrok.a3003.model.DocData
import com.idrok.a3003.model.DocDataChild
import com.idrok.a3003.utils.toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object ReadFromText {

    private var listTitle = arrayListOf<String>()
    private var listData = arrayListOf<DocData>()
    private var listDocChilds = arrayListOf<DocDataChild>()
    private lateinit var context: Context
    private var readFromText: ReadFromText? = null
    var Title = ""


    fun getInstance(context: Context, fileName: String): ReadFromText {
        this.context = context
        parseText(fileName)
        if (readFromText == null) {
            readFromText = ReadFromText
        }
        return readFromText!!
    }

    private fun parseText(fileName: String) {
        listTitle.clear()
        listData.clear()
        try {
            val stream: InputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(stream))
            var words: List<String>
            var index = 0
            reader.forEachLine {
                words = it.split("\t")
                Log.d("ReadFromText", "separated text: $words")
                if (words[0].toIntOrNull() == null || words[0] == "") {
                    listDocChilds.add(DocDataChild(words[1], words[2], words[3], index))
                    Log.d("ReadFromText", "docChilds: $listDocChilds")
                } else {
                    listTitle.add(words[1])
                    listData.add(DocData(words[1], words[2], words[3], words[4]))
                    index = listData.size - 1
                }
            }

            Log.d("ReadFromText", "listData: ${listData.size}")
            stream.close()
        } catch (e: IOException) {
            context.toast("error:${e.message}")
        }
    }

    fun getDataChild(position: Int): ArrayList<DocDataChild> {
        val list = arrayListOf<DocDataChild>()
        for (dataChild in listDocChilds) {
            if (dataChild.index == position) {
                list.add(dataChild)
            }
        }
        return list
    }

    fun getData(position: Int): DocData {
        return listData[position]
    }

    fun getListTitles(): ArrayList<String> {
        return listTitle
    }

}
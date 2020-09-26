package com.idrok.a3003.model

import kotlinx.android.parcel.Parcelize

data class ImageSlider(
    val image:Int = 0
)

const val TITLE_TYPE = 1
const val ITEM_TYPE = 2


data class ListItems(
    val body:String = "",
    val type:Int = 0,
    val listChilds: ListChilds? = null
)

data class ListChilds(
    val name:ArrayList<String> = arrayListOf(),
    val elements:ArrayList<Data> = arrayListOf()
)

data class Data(
    val title:String = "",
    val body: String = ""
)
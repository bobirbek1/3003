package com.idrok.a3003.model

data class ImageSlider(
    val image:Int = 0
)

const val TITLE_TYPE = 1
const val ITEM_TYPE = 2
data class ListItems(
    val body:String = "",
    val type:Int = 0
)
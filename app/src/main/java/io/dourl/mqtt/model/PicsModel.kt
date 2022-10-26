package io.dourl.mqtt

import com.google.gson.annotations.SerializedName

data class PicsModel(
    @SerializedName("total")
    val total : Int,
    @SerializedName("hits")
    val hits : List<Pics>
)

data class Pics(
    val id: Int,
    val tags: String,
    val previewURL: String,
    val largeImageURL: String,
    val fullHDURL: String,
    val imageURL: String,
    val user: String,
    val userImageURL: String,
)

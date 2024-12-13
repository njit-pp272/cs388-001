package com.pp272cs388.flixter_1

import com.google.gson.annotations.SerializedName

class Movie {
    @SerializedName("vote_average")
    var rating: Float = 0.0f

    @JvmField
    @SerializedName("title")
    var title: String? = null

    @SerializedName("poster_path")
    var movie_poster: String? = null

    @SerializedName("overview")
    var overview: String? = null

}
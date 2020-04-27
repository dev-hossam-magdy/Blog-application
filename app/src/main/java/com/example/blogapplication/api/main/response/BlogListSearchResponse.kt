package com.example.blogapplication.api.main.response

import com.example.blogapplication.models.BlogPost
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlogListSearchResponse(
    @SerializedName("results")
    @Expose
    var results:List<BlogPost>,

    @SerializedName("detail")
    @Expose
    var detail:String
) {
    override fun toString(): String {
        return "BlogPostSearchResponse(results=$results, detail='$detail')"
    }
}
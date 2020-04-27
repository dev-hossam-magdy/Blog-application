package com.example.blogapplication.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "blog_post")
data class BlogPost(
    @SerializedName("pk")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "pk")
    var pk: Int,

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title: String,

    @SerializedName("slug")
    @Expose
    @ColumnInfo(name = "slug")
    var slue: String,

    @SerializedName("body")
    @Expose
    @ColumnInfo(name = "body")
    var body: String,

    @SerializedName("image")
    @Expose
    @ColumnInfo(name = "image")
    var image: String,

    @SerializedName("date_update")
    @Expose
    @ColumnInfo(name = "date_update")
    var dateUpdate: Long,

    @SerializedName("username")
    @Expose
    @ColumnInfo(name = "username")
    var username: String

) {
    override fun toString(): String {
        return "BlogPost(pk=$pk, title='$title', slue='$slue', body='$body', image='$image', dateUpdate=$dateUpdate, username='$username')"
    }
}
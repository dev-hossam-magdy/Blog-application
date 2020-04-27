package com.example.blogapplication.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blogapplication.models.BlogPost

@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost: BlogPost):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost:List<BlogPost>)

    @Query("SELECT * FROM blog_post")
    fun getAllBlogPosts():LiveData<List<BlogPost>>
}
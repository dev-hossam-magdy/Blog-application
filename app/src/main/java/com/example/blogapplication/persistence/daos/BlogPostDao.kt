package com.example.blogapplication.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.util.Constants

@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost: BlogPost):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost:List<BlogPost>)

    @Query("""
        SELECT * FROM blog_post 
        WHERE username LIKE '%' || :query || '%'
        OR title LIKE '%' || :query || '%'
        OR body LIKE '%' || :query || '%'
        LIMIT(:page * :pageSize)
        """)
    fun getAllBlogPosts(query: String,page:Int,pageSize:Int = Constants.PAGINATION_PAGE_SIZE):LiveData<List<BlogPost>>
}
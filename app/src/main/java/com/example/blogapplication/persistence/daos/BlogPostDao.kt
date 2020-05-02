package com.example.blogapplication.persistence.daos

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.Constants.PAGINATION_PAGE_SIZE

@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost: BlogPost): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost: List<BlogPost>)

    @Delete
    suspend fun deleteBlogPos(blogPost: BlogPost)

    @Query("""
        UPDATE blog_post SET 
        title = :title , 
        body = :body ,
        image =:image
        WHERE pk = :pk
        
    """)
    fun updateBlogPost(pk:Int , title: String , body:String, image: String)

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE username LIKE '%' || :query || '%'
        OR title LIKE '%' || :query || '%'
        OR body LIKE '%' || :query || '%'
        LIMIT(:page * :pageSize)
        """
    )
    fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_update DESC 
        LIMIT (:page * :pageSize)
        """
    )
    fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_update  ASC 
        LIMIT (:page * :pageSize)"""
    )
    fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY username DESC 
        LIMIT (:page * :pageSize)"""
    )
    fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY username  ASC 
        LIMIT (:page * :pageSize)
        """
    )
    fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>

}
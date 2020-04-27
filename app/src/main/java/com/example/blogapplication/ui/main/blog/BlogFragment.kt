package com.example.blogapplication.ui.main.blog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.blogapplication.R
import com.example.blogapplication.adapters.BlogPostAdapter
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*
import retrofit2.http.Query
import javax.inject.Inject


class BlogFragment : BaseBlogFragment() {

    @Inject
    lateinit var recyclerViewAdapter:BlogPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeObserver()
        executeSearch("")
    }

    private fun executeSearch(query: String){
        viewModel.setStateEvent(BlogPostStateEvent.BlogSearchEvent(query))
    }
    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { blogPostViewState ->
                    Log.e(TAG, "i get the data state")
                    viewModel.setBlogList(blogPostViewState.blogFields.blogList)

                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { blogPostViewState ->
            // set it to wedges
            blogPostViewState?.let {
                val blogPostList =it.blogFields.blogList
                Log.e(TAG, "the data is comming ${blogPostList.size}")
                recyclerViewAdapter.submitList(blogPostList,true)

            }

        })

    }

    private fun initRecyclerView(){
        attachItemSelectedListener()
        blog_post_recyclerview.apply {
            adapter = recyclerViewAdapter

            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val itemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(itemDecoration)
            addItemDecoration(itemDecoration)

            addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == adapter?.itemCount?.minus(1))
                        Log.e(TAG ,"Loading the next page ")
                    //TODO("pagination")
                }
            })
        }

    }

    private fun attachItemSelectedListener() {
        recyclerViewAdapter.itemSelectedListener = object :BlogPostAdapter.ItemSelectedListener{
            override fun onItemSelected(position: Int, item: BlogPost) {
                Log.e(TAG,"onItemSelected position:$position , item:$item ")
                viewModel.setBlogPost(item)
                navigateToDestination(R.id.action_blogFragment_to_viewBlogFragment)
            }
        }
    }



}

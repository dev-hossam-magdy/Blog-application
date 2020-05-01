package com.example.blogapplication.ui.main.blog_fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.blogapplication.R
import com.example.blogapplication.adapters.BlogPostAdapter
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.main.blog.BaseBlogFragment
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState
import com.example.blogapplication.ui.main.blog.viewmodels.*
import com.example.blogapplication.util.ErrorHandling
import com.example.blogapplication.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_blog.*
import javax.inject.Inject


class BlogFragment : BaseBlogFragment() {

    override val TAG: String
        get() = "BlogFragment"

    private var searchQuery = ""
    private lateinit var searchView: SearchView

    @Inject
    lateinit var recyclerViewAdapter: BlogPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        initRecyclerView()
        subscribeObserver()
        setupOnswipeToRefresh()
        if (savedInstanceState == null)
            viewModel.loadFirstPage()
    }

    private fun setupOnswipeToRefresh() {
        swipe_refresh.setOnRefreshListener {
            onBlogSearchOrFilter(this.searchQuery )
            swipe_refresh.isRefreshing = false
        }
    }


    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState?.let {
                handlePagination(it)
                stateChangeListener.onDataStateChange(it)
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { blogPostViewState ->
            // set it to wedges
            blogPostViewState?.let {
                val blogPostList = it.blogFields.blogList
                if (blogPostList.size != 0)
                    Log.e(
                        TAG,
                        "the data is coming viewState.observe ${blogPostList.get(blogPostList.size - 1).pk}"
                    )
                recyclerViewAdapter.submitList(
                    blogPostList,
                    blogPostViewState.blogFields.isQueryExhausted
                )

            }

        })

    }

    private fun initSearchView(menu: Menu) {
        activity?.run {
            val searchManger: SearchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.setSearchableInfo(searchManger.getSearchableInfo(componentName))
            searchView.maxWidth = Int.MAX_VALUE
            searchView.setIconifiedByDefault(true)
            searchView.isSubmitButtonEnabled = true
        }

        // handling button on keyboard

        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        searchPlate.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.searchQuery  = v.text.toString()
                Log.e(
                    TAG,
                    "setOnEditorActionListener (keyboard event) :  Getting the search vlue $searchQuery"
                )

                onBlogSearchOrFilter(searchQuery)
            }
            return@setOnEditorActionListener true
        }

        // handel button event
        (searchView.findViewById(R.id.search_go_btn) as View).setOnClickListener {
            this.searchQuery  = searchPlate.text.toString()
            Log.e(TAG, "setOnClickListener (button event) :  Getting the search vlue $searchQuery")
            onBlogSearchOrFilter(searchQuery)
        }


    }

    private fun handlePagination(dataState: DataState<BlogPostViewState>) {
        dataState.data?.let { event ->
            event.getContentIfNotHandled()?.let { blogPostViewState ->
                viewModel.handelIncomingBlogListData(blogPostViewState)
            }
        }
        if (dataState is DataState.Error)
            dataState.response?.let { event ->
                event.peekContent().message?.let {
                    if (ErrorHandling.isPaginationDone(it)) {
                        event.getContentIfNotHandled()
                        viewModel.setIsQueryExhausted(true)
                    }

                }
            }

    }

    private fun initRecyclerView() {
        attachItemSelectedListener()
        blog_post_recyclerview.apply {
            adapter = recyclerViewAdapter

            layoutManager = LinearLayoutManager(this@BlogFragment.context)
            val itemDecoration = TopSpacingItemDecoration(30)
            removeItemDecoration(itemDecoration)
            addItemDecoration(itemDecoration)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == adapter?.itemCount?.minus(1)) {
                        Log.e(TAG, "Loading the next page ")
                        viewModel.loadNextPage()
                    }
                }
            })
        }

    }

    private fun attachItemSelectedListener() {
        recyclerViewAdapter.itemSelectedListener = object : BlogPostAdapter.ItemSelectedListener {
            override fun onItemSelected(position: Int, item: BlogPost) {
                Log.e(TAG, "onItemSelected position:$position , item:$item ")
                viewModel.setBlogPost(item)
                navigateToDestination(R.id.action_blogFragment_to_viewBlogFragment)
            }
        }
    }

    private fun resetUI() {
        blog_post_recyclerview.smoothScrollToPosition(0)
        stateChangeListener.hideKeyboard()
        focusable_view.requestFocus()
    }

    fun onBlogSearchOrFilter(searchQuery: String) {
        viewModel.resetPage()
        viewModel.setStateEvent(BlogPostStateEvent.BlogSearchEvent(searchQuery))
        resetUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter_settings) {
            showFilterOptions(this.searchQuery)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}

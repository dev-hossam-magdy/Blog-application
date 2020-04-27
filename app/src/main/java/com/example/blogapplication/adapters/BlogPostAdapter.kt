package com.example.blogapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import androidx.room.Index
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.blogapplication.R
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.ui.convertLongToStringDate
import com.example.blogapplication.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*
import javax.inject.Inject

class BlogPostAdapter @Inject constructor(private val requestManager: RequestManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "BlogPostAdapter"
    private val NO_MORE_RESULT_OBJECT = BlogPost(
        pk = -1,
        username = "",
        body = "",
        title = "",
        dateUpdate = 0,
        image = "",
        slue = ""
    )
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {

        override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean =
            oldItem.pk == newItem.pk

        override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean =
            oldItem == newItem

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerViewChangeCallBack(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    internal inner class BlogRecyclerViewChangeCallBack(
        private val adapter: BlogPostAdapter
    ) : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == -1)
            return GenericViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_no_more_results, parent, false)
            )

        return BlogViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_blog_list_item, parent, false), requestManager,itemSelectedListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BlogViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<BlogPost>?, isQueryExhaused: Boolean) {
        list?.let {
            val newList = it.toMutableList()
            if (isQueryExhaused)
                newList.add(NO_MORE_RESULT_OBJECT)
            differ.submitList(newList)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList.get(position).pk
    }

    class BlogViewHolder(itemView: View, private val requestManager: RequestManager,
    private val itemSelectedListener: ItemSelectedListener?) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlogPost) = with(itemView) {
            itemSelectedListener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onItemSelected(adapterPosition, item)
                }
            }

            requestManager
                .load(item.image)
                .transition(withCrossFade())
                .into(itemView.blog_image_item)
            itemView.blog_title_item.text = item.title
            itemView.blog_author_item.text = item.username
            itemView.blog_update_date_item.text = item.dateUpdate.convertLongToStringDate()

        }
    }

    lateinit var itemSelectedListener: ItemSelectedListener

    interface ItemSelectedListener {
        fun onItemSelected(position: Int, item: BlogPost)
    }



}
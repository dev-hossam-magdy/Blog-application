package com.example.blogapplication.ui.main.blog_fragment

import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.blogapplication.R
import com.example.blogapplication.persistence.daos.BlogQueryUtils
import com.example.blogapplication.ui.main.blog.viewmodels.getFilter
import com.example.blogapplication.ui.main.blog.viewmodels.getOrder
import com.example.blogapplication.ui.main.blog.viewmodels.setBlogFilter
import com.example.blogapplication.ui.main.blog.viewmodels.setBlogOrder

fun BlogFragment.showFilterOptions(searchQuery: String) {
    activity?.let {
        val dialog = MaterialDialog(it)
            .noAutoDismiss()
            .customView(R.layout.layout_blog_filter)
        val view = dialog.getCustomView()

        val filter = viewModel.getFilter()

        if (filter.equals(BlogQueryUtils.BLOG_FILTER_DATE_UPDATED))
            view.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_date)
        else
            view.findViewById<RadioGroup>(R.id.filter_group).check(R.id.filter_author)

        val order = viewModel.getOrder()

        if (order.equals(BlogQueryUtils.BLOG_ORDER_ASC))
            view.findViewById<RadioGroup>(R.id.order_group).check(R.id.order_asc)
        else
            view.findViewById<RadioGroup>(R.id.order_group).check(R.id.order_desc)

        view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
            Log.e(TAG, "Flier dialog: positive_button: setOnClickListener:")
            val selectedFilter = dialog.getCustomView().findViewById<RadioButton>(
                dialog.getCustomView()
                    .findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId
            )
            val selectedOrder = dialog.getCustomView().findViewById<RadioButton>(
                dialog.getCustomView()
                    .findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId
            )

            var filter = BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            if (selectedFilter.text.toString().equals(getString(R.string.filter_author)))
                filter = BlogQueryUtils.BLOG_FILTER_USERNAME
            var order = ""
            if (selectedOrder.text.toString().equals(getString(R.string.filter_desc)))
                order = "-"

            viewModel.saveFilterOptions(filter, order).let {
                viewModel.setBlogFilter(filter)
                viewModel.setBlogOrder(order)

                this.onBlogSearchOrFilter(searchQuery)
            }
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
            Log.e(TAG, "Flier dialog: negative_button: setOnClickListener:")
            dialog.dismiss()
        }
        dialog.show()
    }
}
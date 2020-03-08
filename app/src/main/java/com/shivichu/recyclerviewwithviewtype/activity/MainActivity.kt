package com.shivichu.recyclerviewwithviewtype.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivichu.recyclerviewwithviewtype.R
import com.shivichu.recyclerviewwithviewtype.adapter.PaginationRecyclerViewAdapter
import com.shivichu.recyclerviewwithviewtype.model.Model
import com.shivichu.recyclerviewwithviewtype.utils.Constants
import com.shivichu.recyclerviewwithviewtype.utils.PaginationRecyclerViewAdapterCallback
import com.shivichu.recyclerviewwithviewtype.utils.PaginationScrollListener
import java.util.*

class MainActivity : AppCompatActivity(), PaginationRecyclerViewAdapterCallback {

    private var adapter: PaginationRecyclerViewAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isLoading = false
    private val isLast = false
    private var currentPage = PAGE_START
    private val mDataList: MutableList<Model> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val rv = findViewById<View>(R.id.recyclerView) as RecyclerView
        Constants.TYPE = Model.TYPE_2

        adapter = PaginationRecyclerViewAdapter(this)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = linearLayoutManager
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter

        rv.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager!!) {
            override fun loadMoreItems() {

                isLoading = true
                currentPage += 1
                Handler().postDelayed({ loadNextPage() }, 2000)
            }

            override val totalPageCount: Int
                get() = 5
            override val isLastPage: Boolean
                get() = isLast
            override val isCurrentlyLoading: Boolean
                get() = isLoading

        })
        loadFirstPage()
    }

    private fun loadFirstPage() {
        Log.d("TAG", "loadFirstPage: ")
        // To ensure list is visible when retry button in error view is clicked
//        hideErrorView();
        currentPage = PAGE_START
        for (i in 0..19) {
            val model = Model()
            mDataList.add(model)
        }
        adapter!!.addAll(mDataList)
        adapter!!.addLoadingFooter()
    }

    private fun loadNextPage() {
        Log.d("TAG", "loadNextPage: $currentPage")
        adapter!!.removeLoadingFooter()
        isLoading = false
        val size = mDataList.size
        for (i in size..24) {
            val model = Model()
            model.text = "Test $i"
            mDataList.add(model)
        }
        adapter!!.addAll(mDataList)
        adapter!!.addLoadingFooter()
    }

    override fun retryPageLoad() {}

    companion object {
        private const val PAGE_START = 1
        // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
        const val totalPageCount = 5
    }
}
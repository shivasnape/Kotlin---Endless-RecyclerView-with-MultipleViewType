package com.shivichu.recyclerviewwithviewtype.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivichu.recyclerviewwithviewtype.R
import com.shivichu.recyclerviewwithviewtype.model.Model
import com.shivichu.recyclerviewwithviewtype.utils.Constants
import com.shivichu.recyclerviewwithviewtype.utils.PaginationRecyclerViewAdapterCallback
import java.util.*

class PaginationRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var movieResults: MutableList<Model>?
    private var isLoadingAdded = false
    private var retryPageLoad = false
    private val mCallback: PaginationRecyclerViewAdapterCallback
    private var errorMsg: String? = null
    val movies: List<Model>?
        get() = movieResults

    fun setMovies(movieResults: MutableList<Model>?) {
        this.movieResults = movieResults
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            Model.TYPE_LOADING -> {
                val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
            Model.TYPE_1 -> {
                val viewItem = inflater.inflate(R.layout.text_type, parent, false)
                viewHolder = Type1VH(viewItem)
            }
            Model.TYPE_2 -> {
                val viewHero = inflater.inflate(R.layout.item_hero, parent, false)
                viewHolder = Type2VH(viewHero)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = movieResults!![position] // Movie
        when (getItemViewType(position)) {
            Model.TYPE_LOADING -> {
                val loadingVH = holder as LoadingVH
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.visibility = View.VISIBLE
                    loadingVH.mProgressBar.visibility = View.GONE
                    loadingVH.mErrorTxt.text = if (errorMsg != null) errorMsg else "Something went wrong"
                } else {
                    loadingVH.mErrorLayout.visibility = View.GONE
                    loadingVH.mProgressBar.visibility = View.VISIBLE
                }
            }
            Model.TYPE_1 -> {
            }
            Model.TYPE_2 -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (movieResults == null) 0 else movieResults!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            Constants.TYPE
        } else {
            if (position == movieResults!!.size - 1 && isLoadingAdded) Model.TYPE_LOADING else Constants.TYPE
        }
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */
/*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */
    fun add(r: Model) {
        movieResults!!.add(r)
        notifyItemInserted(movieResults!!.size - 1)
    }

    fun addAll(moveResults: List<Model>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: Model?) {
        val position = movieResults!!.indexOf(r)
        if (position > -1) {
            movieResults!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Model())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieResults!!.size - 1
        val result = getItem(position)
        if (result != null) {
            movieResults!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Model {
        return movieResults!![position]
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(movieResults!!.size - 1)
        if (errorMsg != null) this.errorMsg = errorMsg
    }
    /*
   View Holders
   _________________________________________________________________________________________________
    */
    /**
     * Header ViewHolder
     */
    protected inner class Type2VH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    /**
     * Main list's content ViewHolder
     */
    protected inner class Type1VH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    protected inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mProgressBar: ProgressBar
        private val mRetryBtn: ImageButton
        val mErrorTxt: TextView
        val mErrorLayout: LinearLayout
        override fun onClick(view: View) {
            when (view.id) {
                R.id.loadmore_retry, R.id.loadmore_errorlayout -> {
                    showRetry(false, null)
                    mCallback.retryPageLoad()
                }
            }
        }

        init {
            mProgressBar = itemView.findViewById(R.id.loadmore_progress)
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry)
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt)
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout)
            mRetryBtn.setOnClickListener(this)
            mErrorLayout.setOnClickListener(this)
        }
    }

    init {
        mCallback = context as PaginationRecyclerViewAdapterCallback
        movieResults = ArrayList()
    }
}
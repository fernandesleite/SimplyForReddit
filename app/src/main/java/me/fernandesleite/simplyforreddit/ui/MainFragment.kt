package me.fernandesleite.simplyforreddit.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import me.fernandesleite.simplyforreddit.App
import me.fernandesleite.simplyforreddit.R
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.models.Subreddit
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.pagination.DefaultPaginator
import net.dean.jraw.pagination.Paginator

class MainFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private val TAG = "MainFragment"
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SubmissionsAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter


        if (!App.accountHelper.isAuthenticated()){
            viewModel.showFrontPage()
            viewModel.frontPageList.observe(viewLifecycleOwner, Observer {
                adapter.addItems(it.toMutableList())
                isLoading = true
                adapter.notifyDataSetChanged()
            })
        }

        // Code to calculate when the user is seeing the last item copied from my app Meiha:
        // https://github.com/fernandesleite/meiha-android-movie-tracker/blob/master/app/src/main/java/com/moviedb/movieList/MovieListBaseFragment.kt

        fun loadMoreItems() {
            viewModel.nextPage()
            isLoading = false
        }

        fun calcPositionToLoadItems(recyclerView: RecyclerView): Boolean {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            return firstVisible + visibleItemCount >= totalItemCount
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                } else if (dy > 0) {

                }
                if (calcPositionToLoadItems(recyclerView) && isLoading) {
                    Log.i(TAG, "onViewCreated: calc")
                    loadMoreItems()

                }
            }
        })
    }
}

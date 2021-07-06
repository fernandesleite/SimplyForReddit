package me.fernandesleite.simplyforreddit.ui.home

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class MainFragmentBase(): Fragment() {
    abstract var isLoading: Boolean
    abstract fun loadMoreItems()

    // Code to calculate when the user is seeing the last item copied from my app Meiha:
    // https://github.com/fernandesleite/meiha-android-movie-tracker/blob/master/app/src/main/java/com/moviedb/movieList/MovieListBaseFragment.kt

    fun calcPositionToLoadItems(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        return firstVisible + visibleItemCount >= totalItemCount
    }

    fun addOnScrollListener(recyclerView: RecyclerView){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (calcPositionToLoadItems(recyclerView) && isLoading) {
                    loadMoreItems()
                }
            }
        })
    }
}
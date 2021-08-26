package me.fernandesleite.simplyforreddit.ui.home

import android.view.Gravity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.MainActivity
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter

abstract class BaseMainFragment(): Fragment() {
    abstract var isLoading: Boolean
    abstract fun loadMoreItems()
    abstract fun setupData(recyclerView: RecyclerView)

    fun setupToolbar(toolbar: Toolbar, toolbarTitle: String) {
        toolbar.apply {
            title = toolbarTitle
            toolbar.setNavigationOnClickListener {
                val act = activity as MainActivity
                act.openDrawer()
            }
            inflateMenu(R.menu.toolbar_menu)
            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.search_button) {
                    findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
                    true
                } else if (item.itemId == R.id.filter) {
                    val popup = PopupMenu(context, toolbar, Gravity.END)
                    popup.inflate(R.menu.toolbar_filter)
                    popup.show()
                    true
                }
                else false
            }

        }
    }

    fun setupRecyclerView(recyclerView: RecyclerView, adapter: SubmissionsAdapter){
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        calcPositionToLoadItems(recyclerView)
        addOnScrollListener(recyclerView)
    }


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
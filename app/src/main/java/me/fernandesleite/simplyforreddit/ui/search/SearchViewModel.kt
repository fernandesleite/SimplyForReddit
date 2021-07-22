package me.fernandesleite.simplyforreddit.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fernandesleite.simplyforreddit.App
import me.fernandesleite.simplyforreddit.repository.RedditRepository
import net.dean.jraw.models.*
import net.dean.jraw.pagination.DefaultPaginator
import net.dean.jraw.pagination.SearchPaginator
import net.dean.jraw.pagination.SubredditSearchPaginator

class SearchViewModel(private val repository: RedditRepository): ViewModel() {
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private val _subredditSearchResults = MutableLiveData<List<SubredditSearchResult>>()
    val subredditSearchResults: LiveData<List<SubredditSearchResult>>
        get() = _subredditSearchResults

    fun showSearch(query: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _subredditSearchResults.postValue(repository.getSearchSubredditResults(query))
            }
        }
    }
}
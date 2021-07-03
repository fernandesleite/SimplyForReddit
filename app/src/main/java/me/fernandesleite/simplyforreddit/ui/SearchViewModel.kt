package me.fernandesleite.simplyforreddit.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fernandesleite.simplyforreddit.App
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.models.Subreddit
import net.dean.jraw.models.SubredditSearchSort
import net.dean.jraw.pagination.DefaultPaginator
import net.dean.jraw.pagination.SearchPaginator
import net.dean.jraw.pagination.SubredditSearchPaginator

class SearchViewModel: ViewModel() {
    val redditClient = App.accountHelper.reddit
    private lateinit var paginator: SubredditSearchPaginator
    val uiScope = CoroutineScope(Dispatchers.Main)
    private val _subredditSearchResults = MutableLiveData<Listing<Subreddit>>()
    val subredditSearchResults: LiveData<Listing<Subreddit>>
        get() = _subredditSearchResults

    fun showSearch(query: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val searchResults = redditClient.searchSubreddits().query(query)
                paginator = searchResults.build()
                val fp = paginator.next()
                _subredditSearchResults.postValue(fp)
                Log.i("SearchViewModel", "showSearch: ${fp[0].name}")
            }
        }


    }
}
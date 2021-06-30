package me.fernandesleite.simplyforreddit.ui

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
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.pagination.DefaultPaginator

class HomeViewModel : ViewModel() {
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val helper = App.accountHelper

    private val _frontPageList = MutableLiveData<Listing<Submission>>()
    val frontPageList: LiveData<Listing<Submission>>
        get() = _frontPageList

    private lateinit var paginator: DefaultPaginator<Submission>

    fun showFrontPage() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                helper.switchToUserless()
                val paginationBuilder: DefaultPaginator.Builder<Submission, SubredditSort> =
                    helper.reddit.frontPage()
                paginator = paginationBuilder.build()
                val fp: Listing<Submission> = paginator.next()
                _frontPageList.postValue(fp)
            }
        }
    }

    fun nextPage() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val fp: Listing<Submission> = paginator.next()
                _frontPageList.postValue(fp)
            }
        }
    }
}
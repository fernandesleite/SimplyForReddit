package me.fernandesleite.simplyforreddit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fernandesleite.simplyforreddit.App
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.pagination.DefaultPaginator

open class SharedSubmissionViewModel : SharedViewModelBase() {
    private lateinit var paginatorFrontPage: DefaultPaginator<Submission>
    private lateinit var paginatorSubreddit: DefaultPaginator<Submission>
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val helper = App.accountHelper
    private val localListFrontPageSubmissions = mutableListOf<Submission>()
    private val localListSubredditSubmissions = mutableListOf<Submission>()

    private val _submission = MutableLiveData<Submission>()
    val submission: LiveData<Submission>
        get() = _submission

    private val _listOfFrontPageSubmissions = MutableLiveData<List<Submission>>()
    val listOfFrontPageSubmissions: LiveData<List<Submission>>
        get() = _listOfFrontPageSubmissions

    private val _listOfSubredditSubmissions = MutableLiveData<List<Submission>>()
    val listOfSubredditSubmissions: LiveData<List<Submission>>
        get() = _listOfSubredditSubmissions

    fun setSubmission(submission: Submission) {
        _submission.value = submission
    }

    init {
        showFrontPage()
    }

    private fun showFrontPage() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                helper.switchToUserless()
                val paginationBuilder: DefaultPaginator.Builder<Submission, SubredditSort> =
                    helper.reddit.frontPage()
                paginatorFrontPage = paginationBuilder.build()
                paginatorFrontPage.next().forEach {
                    localListFrontPageSubmissions.add(it)
                }
                helper.reddit.requestStub()
                _listOfFrontPageSubmissions.postValue(localListFrontPageSubmissions)
            }
        }
    }

    fun nextPageFrontPage() {
        nextPageBase(_listOfFrontPageSubmissions, localListFrontPageSubmissions, paginatorFrontPage)
    }

    fun nextPageSubreddit() {
        nextPageBase(_listOfSubredditSubmissions, localListSubredditSubmissions, paginatorSubreddit)
    }

    fun showSubreddit(subredditName: String) {
        if (localListSubredditSubmissions.isNotEmpty() && localListSubredditSubmissions[0].subreddit != subredditName) {
            localListSubredditSubmissions.clear()
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val paginationBuilder: DefaultPaginator.Builder<Submission, SubredditSort> =
                    helper.reddit.subreddit(subredditName).posts()
                paginatorSubreddit = paginationBuilder.build()
                paginatorSubreddit.next().forEach {
                    localListSubredditSubmissions.add(it)
                }
                _listOfSubredditSubmissions.postValue(localListSubredditSubmissions)
            }
        }
    }
}
package me.fernandesleite.simplyforreddit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fernandesleite.simplyforreddit.repository.RedditRepository
import net.dean.jraw.models.Submission
import net.dean.jraw.pagination.DefaultPaginator

class SharedSubmissionViewModel(private val repository: RedditRepository) :
    BaseSharedViewModel() {
    private lateinit var paginatorFrontPage: DefaultPaginator<Submission>
    private lateinit var paginatorSubreddit: DefaultPaginator<Submission>
    private val uiScope = CoroutineScope(Dispatchers.Main)
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
                paginatorFrontPage = repository.getFrontPagePosts()
                updateFrontPage()
            }
        }
    }

    fun updateFrontPage() {
        updateCurrentList(_listOfFrontPageSubmissions, localListFrontPageSubmissions, paginatorFrontPage)
    }

    fun updateSubreddit() {
        updateCurrentList(_listOfSubredditSubmissions, localListSubredditSubmissions, paginatorSubreddit)
    }

    fun showSubreddit(subredditName: String) {
        if (localListSubredditSubmissions.isNotEmpty() && localListSubredditSubmissions[0].subreddit != subredditName) {
            localListSubredditSubmissions.clear()
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                paginatorSubreddit = repository.getSubredditPosts(subredditName)
                updateSubreddit()
            }
        }
    }

    fun isAuthenticated(): String =  repository.isAuthenticated()

}
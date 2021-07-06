package me.fernandesleite.simplyforreddit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fernandesleite.simplyforreddit.App
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.pagination.DefaultPaginator

open class SharedSubmissionViewModel : ViewModel() {
    private lateinit var paginator: DefaultPaginator<Submission>
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val helper = App.accountHelper
    private val listSubmission = mutableListOf<Submission>()

    private val _submission = MutableLiveData<Submission>()
    val submission: LiveData<Submission>
        get() = _submission

    private val _listOfFrontPageSubmissions = MutableLiveData<List<Submission>>()
    val listOfFrontPageSubmissions: LiveData<List<Submission>>
        get() = _listOfFrontPageSubmissions

    fun setSubmission(submission: Submission) {
        _submission.value = submission
    }

    init {
        showFrontPage()
    }

    fun showFrontPage() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                helper.switchToUserless()

                val paginationBuilder: DefaultPaginator.Builder<Submission, SubredditSort> =
                    helper.reddit.frontPage()
                paginator = paginationBuilder.build()
                paginator.next().forEach {
                    listSubmission.add(it)
                }
                _listOfFrontPageSubmissions.postValue(listSubmission)
            }
        }
    }

    fun nextPage() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                paginator.next().forEach {
                    listSubmission.add(it)
                }
                _listOfFrontPageSubmissions.postValue(listSubmission)
            }
        }
    }
}
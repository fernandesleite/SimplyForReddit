package me.fernandesleite.simplyforreddit.repository

import androidx.lifecycle.MutableLiveData
import me.fernandesleite.simplyforreddit.App
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.pagination.DefaultPaginator

private val helper = App.accountHelper

class RedditRepository {

    private fun switchToAnon() = helper.switchToUserless()

    fun isAuthenticated() = helper.reddit.requireAuthenticatedUser()

    fun getFrontPagePosts(): DefaultPaginator<Submission> {
        switchToAnon()
        helper.reddit.requestStub()
        return helper.reddit.frontPage().build()
    }

    fun getSubredditPosts(subredditName: String): DefaultPaginator<Submission> = helper.reddit.subreddit(subredditName).posts().build()

    fun getAllPosts(): DefaultPaginator<Submission> = helper.reddit.subreddit("all").posts().build()

    fun getPopularPosts(): DefaultPaginator<Submission> = helper.reddit.subreddit("popular").posts().build()

    fun getSearchSubredditResults(query: String) = helper.reddit.searchSubredditsByName(query)

}


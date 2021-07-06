package me.fernandesleite.simplyforreddit.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter
import net.dean.jraw.models.Submission

class SubredditFragment : MainFragmentBase(), SubmissionsAdapter.OnClickListener {

    val args: SubredditFragmentArgs by navArgs()

    private val sharedSubmissionViewModel: SharedSubmissionViewModelBase by activityViewModels()
    private lateinit var adapter: SubmissionsAdapter

    override var isLoading = false
    override fun loadMoreItems() {
        sharedSubmissionViewModel.nextPageSubreddit()
        isLoading = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = SubmissionsAdapter(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subreddit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        sharedSubmissionViewModel.showSubreddit(args.subreddit)

        toolbar.title = args.subreddit
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.search_button) {
                findNavController().navigate(R.id.action_subredditFragment_to_searchFragment)
                true
            } else false
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter

        // workaround using the fragment as a owner to maintain recyclerview pagination after leaving screen
        sharedSubmissionViewModel.listOfSubredditSubmissions.observe(this, {
            adapter.submitList(it)
            isLoading = true
            adapter.notifyDataSetChanged()
        })

        calcPositionToLoadItems(recyclerView)
        addOnScrollListener(recyclerView)
    }

    override fun onSubmissionClick(submission: Submission) {
        sharedSubmissionViewModel.setSubmission(submission)
        findNavController().navigate(R.id.action_subredditFragment_to_submissionFragment)
    }
}
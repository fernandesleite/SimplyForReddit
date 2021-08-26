package me.fernandesleite.simplyforreddit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter
import net.dean.jraw.models.Submission
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf


class SubredditFragment : BaseMainFragment(), SubmissionsAdapter.OnClickListener {

    val args: SubredditFragmentArgs by navArgs()

    private val sharedSubmissionViewModel: SharedSubmissionViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private val adapter: SubmissionsAdapter by inject {
        parametersOf(this)
    }

    override var isLoading = false
    override fun loadMoreItems() {
        sharedSubmissionViewModel.updateSubreddit()
        isLoading = false
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
        recyclerView = view.findViewById(R.id.recycler_view)
        if (args.subreddit == "no_subreddit_selected") {
            findNavController().navigate(
                SubredditFragmentDirections.actionSubredditFragmentToErrorFragment(
                    "Join a subreddit to see the list!"
                )
            )
        } else {

            setupToolbar(toolbar)
            setupData(recyclerView)
            setupRecyclerView(recyclerView, adapter)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        toolbar.title = args.subreddit
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.search_button) {
                findNavController().navigate(R.id.action_subredditFragment_to_searchFragment)
                true
            } else false
        }
    }

    override fun setupData(recyclerView: RecyclerView) {
        if (adapter.itemCount <= 0) {
            sharedSubmissionViewModel.showSubreddit(args.subreddit)
        }
        // workaround using the fragment as a owner to maintain recyclerview pagination after leaving screen
        sharedSubmissionViewModel.listOfSubredditSubmissions.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            isLoading = true
            adapter.notifyDataSetChanged()
            sharedSubmissionViewModel.recyclerViewStateParcel.observe(viewLifecycleOwner, { parcelable ->
                recyclerView.layoutManager?.onRestoreInstanceState(parcelable)
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        recyclerViewState?.let { sharedSubmissionViewModel.recyclerViewStateParcel.value = it }
    }

    override fun onSubmissionClick(submission: Submission) {
        sharedSubmissionViewModel.setSubmission(submission)
        findNavController().navigate(R.id.action_subredditFragment_to_submissionFragment)
    }
}
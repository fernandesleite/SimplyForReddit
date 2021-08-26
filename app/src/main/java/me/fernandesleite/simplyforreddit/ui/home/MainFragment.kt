package me.fernandesleite.simplyforreddit.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter
import net.dean.jraw.models.Submission
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class MainFragment : BaseMainFragment(), SubmissionsAdapter.OnClickListener {

    private val sharedSubmissionViewModel: SharedSubmissionViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private val adapter: SubmissionsAdapter by inject {
        parametersOf(this)
    }
    private val TAG = "MainFragment"

    override var isLoading = false
    override fun loadMoreItems() {
        sharedSubmissionViewModel.updateFrontPage()
        isLoading = false
    }

    override fun setupData(recyclerView: RecyclerView) {
        sharedSubmissionViewModel.listOfFrontPageSubmissions.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            isLoading = true
            adapter.notifyDataSetChanged()
            sharedSubmissionViewModel.recyclerViewStateParcel.observe(viewLifecycleOwner, { parcelable ->
                recyclerView.layoutManager?.onRestoreInstanceState(parcelable)
            })
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        recyclerView = view.findViewById(R.id.recycler_view)
        setupToolbar(toolbar, "Front Page")
        setupData(recyclerView)
        setupRecyclerView(recyclerView, adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        recyclerViewState?.let { sharedSubmissionViewModel.recyclerViewStateParcel.value = it }
    }

    override fun onSubmissionClick(submission: Submission) {
        sharedSubmissionViewModel.setSubmission(submission)
        findNavController().navigate(R.id.action_mainFragment_to_submissionFragment)
    }
}
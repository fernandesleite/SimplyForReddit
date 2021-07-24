package me.fernandesleite.simplyforreddit.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.MainActivity
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter
import net.dean.jraw.models.Submission
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class MainFragment : BaseMainFragment(), SubmissionsAdapter.OnClickListener {

    private val sharedSubmissionViewModel: SharedSubmissionViewModel by sharedViewModel()
    private val adapter: SubmissionsAdapter by inject {
        parametersOf(this)
    }
    private val TAG = "MainFragment"

    override var isLoading = false
    override fun loadMoreItems() {
        sharedSubmissionViewModel.updateFrontPage()
        isLoading = false
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

        toolbar.apply {
            title = "Front Page"
            toolbar.setNavigationOnClickListener {
                val act = activity as MainActivity
                act.openDrawer()
            }
            inflateMenu(R.menu.toolbar_menu)
            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.search_button) {
                    findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
                    true
                } else if (item.itemId == R.id.filter) {
                    val popup = PopupMenu(context, toolbar, Gravity.END)
                    popup.inflate(R.menu.toolbar_filter)
                    popup.show()
                    true
                }
                else false
            }

        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter

        // workaround using the fragment as a owner to maintain recyclerview pagination after leaving screen
        sharedSubmissionViewModel.listOfFrontPageSubmissions.observe(this, {
            adapter.submitList(it)
            isLoading = true
            adapter.notifyDataSetChanged()
        })

        calcPositionToLoadItems(recyclerView)
        addOnScrollListener(recyclerView)
    }

    override fun onSubmissionClick(submission: Submission) {
        sharedSubmissionViewModel.setSubmission(submission)
        findNavController().navigate(R.id.action_mainFragment_to_submissionFragment)
    }
}
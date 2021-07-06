package me.fernandesleite.simplyforreddit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter
import net.dean.jraw.models.Submission

class MainFragment : Fragment(), SubmissionsAdapter.OnClickListener {

    private val sharedSubmissionViewModel: SharedSubmissionViewModel by activityViewModels()
    private lateinit var adapter: SubmissionsAdapter
    private val TAG = "MainFragment"
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = SubmissionsAdapter(this)
        super.onCreate(savedInstanceState)
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
        toolbar.title = "Front Page"
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.search_button) {
                findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
                true
            } else false
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter

        // workaround using the fragment as a owner to maintain recyclerview pagination after leaving screen
        sharedSubmissionViewModel.listOfFrontPageSubmissions.observe(this, {
            adapter.submitList(it)
            isLoading = true
            adapter.notifyDataSetChanged()
        })

        // Code to calculate when the user is seeing the last item copied from my app Meiha:
        // https://github.com/fernandesleite/meiha-android-movie-tracker/blob/master/app/src/main/java/com/moviedb/movieList/MovieListBaseFragment.kt

        fun loadMoreItems() {
            sharedSubmissionViewModel.nextPage()
            isLoading = false
        }

        fun calcPositionToLoadItems(recyclerView: RecyclerView): Boolean {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            return firstVisible + visibleItemCount >= totalItemCount
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (calcPositionToLoadItems(recyclerView) && isLoading) {
                    loadMoreItems()
                }
            }
        })
    }

    override fun onSubmissionClick(submission: Submission) {
        sharedSubmissionViewModel.setSubmission(submission)
        findNavController().navigate(R.id.action_mainFragment_to_submissionFragment)
    }
}

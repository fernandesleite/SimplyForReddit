package me.fernandesleite.simplyforreddit.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : Fragment(), SearchAdapter.OnClickListener {

    private val viewModel: SearchViewModel by viewModel()
    private val adapter: SearchAdapter by inject {
        parametersOf(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        NavigationUI.setupWithNavController(
            toolbar,
            NavHostFragment.findNavController(requireParentFragment())
        )
        val recyclerView = view.findViewById<RecyclerView>(R.id.searchResults)
        recyclerView.adapter = adapter
        toolbar.inflateMenu(R.menu.search_bar)
        val searchBar = toolbar.menu.findItem(R.id.search_bar)
        val sv = searchBar.actionView as SearchView

        sv.apply {
            setIconifiedByDefault(false)
            requestFocus()
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.showSearch(newText ?: "")
                    viewModel.subredditSearchResults.observe(viewLifecycleOwner, {
                        adapter.submitList(it)
                        adapter.notifyDataSetChanged()
                    })
                    return false
                }

            })
        }
    }

    override fun onSubredditClick(subredditName: String) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSubredditFragment(subredditName))
    }
}
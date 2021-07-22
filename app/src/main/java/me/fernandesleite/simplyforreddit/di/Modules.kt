package me.fernandesleite.simplyforreddit.di

import me.fernandesleite.simplyforreddit.ui.home.SharedSubmissionViewModel
import me.fernandesleite.simplyforreddit.ui.search.SearchAdapter
import me.fernandesleite.simplyforreddit.ui.search.SearchViewModel
import me.fernandesleite.simplyforreddit.ui.submission.SubmissionsAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val adaptersModule = module {
    single { (clickListener: SubmissionsAdapter.OnClickListener) ->
        SubmissionsAdapter(clickListener)
    }
    single { (clickListener: SearchAdapter.OnClickListener) ->
        SearchAdapter(clickListener)
    }
}

val mainModule = module {
    viewModel {
        SearchViewModel()
    }
    viewModel {
        SharedSubmissionViewModel()
    }
}
package me.fernandesleite.simplyforreddit.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fernandesleite.simplyforreddit.repository.RedditRepository
import net.dean.jraw.models.Submission
import net.dean.jraw.pagination.DefaultPaginator

open class BaseSharedViewModel() : ViewModel() {
    val uiScope = CoroutineScope(Dispatchers.Main)

    fun updateCurrentList(
        listLiveData: MutableLiveData<List<Submission>>,
        viewModelList: MutableList<Submission>,
        paginator: DefaultPaginator<Submission>
    ) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                paginator.next().forEach {
                    viewModelList.add(it)
                }
                listLiveData.postValue(viewModelList)
            }
        }
    }
}
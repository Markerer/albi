package albi.bme.hu.albi.viewmodel

import albi.bme.hu.albi.controller.AppController
import albi.bme.hu.albi.datasource.factory.FeedDataFactory
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.NetworkState
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList


import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FeedViewModel(private val appController: AppController) : ViewModel() {

    private var executor: Executor? = null
    var networkState: LiveData<NetworkState>? = null
        private set
    var articleLiveData: LiveData<PagedList<Flat>>? = null
        private set

    init {
        init()
    }

    private fun init() {
        executor = Executors.newFixedThreadPool(5)

        val feedDataFactory = FeedDataFactory(appController)
        networkState = Transformations.switchMap(feedDataFactory.mutableLiveData
        ) { dataSource -> dataSource.networkState }

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build() // alapból 20

        articleLiveData = LivePagedListBuilder(feedDataFactory, pagedListConfig)
                .setFetchExecutor(executor!!)
                .build()
    }
}

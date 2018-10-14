package albi.bme.hu.albi.datasource.factory

import albi.bme.hu.albi.controller.AppController
import albi.bme.hu.albi.datasource.FeedDataSource
import albi.bme.hu.albi.model.Flat
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

class FeedDataFactory(private val appController: AppController) : DataSource.Factory<Long,Flat>() {

     val mutableLiveData: MutableLiveData<FeedDataSource> = MutableLiveData()
    private var feedDataSource: FeedDataSource? = null

    override fun create(): DataSource<Long, Flat> {
        feedDataSource = FeedDataSource(appController)
        mutableLiveData.postValue(feedDataSource)
        return feedDataSource as FeedDataSource
    }
}

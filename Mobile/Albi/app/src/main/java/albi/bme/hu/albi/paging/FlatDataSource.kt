package albi.bme.hu.albi.paging

import albi.bme.hu.albi.model.Flat
import android.arch.paging.PositionalDataSource



class FlatDataSource : PositionalDataSource<Flat>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Flat>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Flat>) {

    }
}
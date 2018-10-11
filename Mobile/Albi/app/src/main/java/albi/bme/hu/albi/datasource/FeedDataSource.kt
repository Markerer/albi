package albi.bme.hu.albi.datasource

import albi.bme.hu.albi.controller.AppController
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.NetworkState
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/***
 * A Flat osztályt lehet, hogy meg kéne úgy csinálni, mint a Feedet a
 * másikban (implements Parcelable) ;|;
 */


class FeedDataSource(var appController: AppController) : PageKeyedDataSource<Long, Flat>() {

    var networkState: MutableLiveData<NetworkState> = MutableLiveData()
    var initialLoading: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Flat>) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        appController.restApi?.getMainFlatsByPage(1 /*ide majd valami paraméter kell*/)
                ?.enqueue(object : Callback<List<Flat>> {
                    override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                        if (response.isSuccessful) {
                            callback.onResult(response.body() as MutableList<Flat>, null, 2L)
                            initialLoading.postValue(NetworkState.LOADED)
                            networkState.postValue(NetworkState.LOADED)

                        } else {
                            initialLoading.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                            networkState.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                        }
                    }


                    override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                        val errorMessage = if (t == null) "unknown error" else t.message
                        networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                    }
                })

    }


    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Flat>) {
        networkState.postValue(NetworkState.LOADING)

        appController.restApi?.getMainFlatsByPage(2/*innen kéne felfele számolni?*/)
                ?.enqueue(object: Callback<List<Flat>>{
                    override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                        val errorMessage = if (t == null) "unknown error" else t.message
                        networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                    }

                    override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                        if (response.isSuccessful) {
                            callback.onResult(response.body() as MutableList<Flat>, params.key+1)
                            networkState.postValue(NetworkState.LOADED)

                        } else
                            networkState.postValue(NetworkState(NetworkState.Status.FAILED, response.message()))
                    }
                })

    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Flat>) {
    }
}
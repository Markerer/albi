package albi.bme.hu.albi.controller

import albi.bme.hu.albi.interfaces.main.FlatClient
import albi.bme.hu.albi.network.RestApiFactory
import android.app.Application
import android.content.Context
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers



class AppController : Application() {

    var restApi: FlatClient? = null
        get() {
            if (field == null) {
                this.restApi = RestApiFactory.create()
            }
            return field
        }
    private var scheduler: Scheduler? = null

    override fun onCreate() {
        super.onCreate()
    }

    fun subscribeScheduler(): Scheduler? {
        if (scheduler == null) {
            scheduler = Schedulers.io()
        }

        return scheduler
    }

    fun setScheduler(scheduler: Scheduler) {
        this.scheduler = scheduler
    }

    companion object {


        private operator fun get(context: Context): AppController {
            return context.applicationContext as AppController
        }

        fun create(context: Context): AppController {
            return AppController[context]
        }
    }
}

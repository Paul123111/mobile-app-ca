package ie.setu.appstore.main

import android.app.Application
import ie.setu.appstore.models.AppModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    var apps = ArrayList<AppModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Appstore started")
        apps.add(AppModel("Appstore"))
    }
}
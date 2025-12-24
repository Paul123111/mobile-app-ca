package ie.setu.appstore.main

import android.app.Application
import android.content.Context
import androidx.core.splashscreen.SplashScreen
import ie.setu.appstore.R
import ie.setu.appstore.models.AppJSONStoreV2
import ie.setu.appstore.models.AppStore
import kotlinx.serialization.json.*
import timber.log.Timber
import timber.log.Timber.i
import java.io.File
import java.io.FileOutputStream

class MainApp : Application() {
    lateinit var apps: AppStore
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        apps = AppJSONStoreV2(applicationContext)
        i("Appstore started")
    }

    // loads the res/raw/apps.json folder for debugging
    fun writeDebugApps() {
        val file = File(context.filesDir, "apps.json")
        val debugFile = resources.openRawResource(R.raw.apps)
        val writer = FileOutputStream(file)
        writer.write(debugFile.readBytes())
        writer.close()
    }
}
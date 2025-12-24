package ie.setu.appstore.main

import android.app.Application
import android.content.Context
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import ie.setu.appstore.models.AppJsonStore
import ie.setu.appstore.models.AppMemStore
import ie.setu.appstore.models.AppModel
import org.json.JSONObject
import timber.log.Timber
import timber.log.Timber.i
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import kotlinx.serialization.json.*
import ie.setu.appstore.R
import ie.setu.appstore.models.AppJSONStoreV2
import ie.setu.appstore.models.AppStore

class MainApp : Application() {
    lateinit var apps: AppStore
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        apps = AppJSONStoreV2(applicationContext)
        i("Appstore started")

//        context = baseContext as Context

//        val file = File(context.filesDir, "apps.json")
//        if (!file.exists()) {
//            writeDebugApps()
//        }

//        apps.setJsonContext(context)
//        apps.loadFromFile()

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
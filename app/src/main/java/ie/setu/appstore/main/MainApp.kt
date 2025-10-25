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

class MainApp : Application() {
    var apps = AppJsonStore()
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Appstore started")

        context = baseContext as Context

        apps.setJsonContext(context)
        apps.loadFromFile()

    }
}
package ie.setu.appstore.main

import android.app.Application
import android.content.Context
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
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
    var apps = AppMemStore()
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Appstore started")
        apps.create(AppModel(0, "Appstore"))
        apps.create(AppModel(0, "App 2"))

        context = baseContext as Context

        val file = File(context.filesDir, "apps.json")
        i(file.absolutePath)
        val writer = FileOutputStream(file, true)
        val read = JsonReader(file.reader())
        writer.write("123\n1".toByteArray())
        writer.close()

        i(Json.encodeToString(AppModel(0, "hi")))

        i(file.readLines().toString())
        i(filesDir.toString())

    }
}
package ie.setu.appstore.models

import android.content.Context
import kotlinx.serialization.json.Json
import timber.log.Timber.i
import java.io.File
import java.io.FileOutputStream
import java.util.function.Predicate

class AppJsonStore : AppStore, JsonStore<AppModel> {
    var apps = ArrayList<AppModel>()
    var lastRemovedId: Int = -1
    override lateinit var context: Context

    override fun search(query: String): ArrayList<AppModel> {
        val appsList = ArrayList<AppModel>()
        for (app in apps) {
            if ((app.name.lowercase()).contains(query.lowercase())) {
                appsList.add(app.copy())
            }
        }
        return appsList
    }

    override fun sort(c: Comparator<AppModel>) {
        apps.sortWith(c)
    }

    override fun filter(p: (AppModel) -> Boolean): ArrayList<AppModel> {
        return ArrayList(apps.filter(p))
    }

    override fun loadFromFile() {
        val appsList = readFromFile()
        i(appsList.toString())
        for (app in appsList) {
            app.id = getId()
            apps.add(app.copy())
        }
    }

    override fun findAll(): List<AppModel> {
        return apps
    }

    override fun create(app: AppModel) {
        app.id = getId()
        apps.add(app)
        writeToFile()
        logAll()
    }

    override fun update(app: AppModel) {
        var foundApp: AppModel? = apps.find { a -> a.id == app.id }
        if (foundApp != null) {
            foundApp.name = app.name
            foundApp.appType = app.appType
            foundApp.price = app.price
            foundApp.ratings = app.ratings
            writeToFile()
            logAll()
        }
    }

    override fun delete(id: Int) {
        var foundApp: AppModel? = apps.find { a -> a.id == id }
        if (foundApp != null) {
            apps.remove(foundApp)
            lastRemovedId = id
            writeToFile()
            logAll()
        }
    }

    fun logAll() {
        apps.forEach{ i("$it") }
    }

    override fun readFromFile(): ArrayList<AppModel> {
        val file = File(context.filesDir, "apps.json")
        i(file.readLines().joinToString("\n"))
        return Json.decodeFromString<ArrayList<AppModel>>(file.readLines().joinToString("\n"))
    }

    override fun writeToFile() {
        val file = File(context.filesDir, "apps.json")
        val writer = FileOutputStream(file)
        writer.write(Json.encodeToString(apps).toByteArray())
        writer.close()
    }

    override fun setJsonContext(context: Context) {
        this.context = context
    }

}
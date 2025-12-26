package ie.setu.appstore.models

import timber.log.Timber.i
import java.util.function.Predicate

var lastId = 0

internal fun getId(): Int {
    return lastId++
}

// no longer used, replace with AppJsonStore
class AppMemStore: AppStore {
    val apps = ArrayList<AppModel>()
    var lastRemovedId: Int = -1

    override fun findAll(): List<AppModel> {
        return apps
    }

    override fun create(app: AppModel) {
        app.id = getId()
        apps.add(app)
        logAll()
    }

    override fun update(app: AppModel) {
        var foundApp: AppModel? = apps.find { a -> a.id == app.id }
        if (foundApp != null) {
            foundApp.name = app.name
            logAll()
        }
    }

    override fun delete(id: Int) {
        var foundApp: AppModel? = apps.find { a -> a.id == id }
        if (foundApp != null) {
            apps.remove(foundApp)
            lastRemovedId = id
            logAll()
        }
    }

    override fun delete(app: AppModel) {
        TODO("Not yet implemented")
    }

    override fun search(query: String): List<AppModel> {
        TODO("unused")
    }

    override fun sort(c: Comparator<AppModel>) {
        TODO("unused")
    }

    override fun filter(p: (AppModel) -> Boolean): ArrayList<AppModel> {
        TODO("Not yet implemented")
    }

    fun logAll() {
        apps.forEach{ i("$it") }
    }
}
package ie.setu.appstore.models

import timber.log.Timber.i

var lastId = 0

internal fun getId(): Int {
    return lastId++
}

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

    fun logAll() {
        apps.forEach{ i("$it") }
    }
}
package ie.setu.appstore.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class AppMemStore: AppStore {
    val apps = ArrayList<AppModel>()

    override fun findAll(): List<AppModel> {
        return apps
    }

    override fun create(app: AppModel) {
        app.id = getId()
        apps.add(app)
        logAll()
    }

    override fun update(app: AppModel) {
        var foundPlacemark: AppModel? = apps.find { p -> p.id == app.id }
        if (foundPlacemark != null) {
            foundPlacemark.name = app.name
            logAll()
        }
    }

    fun logAll() {
        apps.forEach{ i("$it") }
    }
}
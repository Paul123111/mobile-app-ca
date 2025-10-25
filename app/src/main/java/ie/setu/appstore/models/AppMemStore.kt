package ie.setu.appstore.models

import timber.log.Timber.i

class AppMemStore: AppStore {
    val apps = ArrayList<AppModel>()

    override fun findAll(): List<AppModel> {
        return apps
    }

    override fun create(app: AppModel) {
        apps.add(app.copy())
        logAll()
    }

    fun logAll() {
        apps.forEach{ i("$it") }
    }
}
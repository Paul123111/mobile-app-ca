package ie.setu.appstore.models

interface AppStore {
    fun findAll(): List<AppModel>
    fun create(app: AppModel)
    fun update(app: AppModel)
    fun delete(id: Int)
}
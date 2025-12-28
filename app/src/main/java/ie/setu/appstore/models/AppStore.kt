package ie.setu.appstore.models

import java.util.function.Predicate

interface AppStore {
    fun findAll(): List<AppModel>
    fun create(app: AppModel)
    fun update(app: AppModel)
    fun delete(id: Int)
    fun delete(app: AppModel)
    fun search(query: String): List<AppModel>
    fun sort(c: Comparator<AppModel>)
    fun filter(p: (AppModel) -> Boolean): List<AppModel>
}
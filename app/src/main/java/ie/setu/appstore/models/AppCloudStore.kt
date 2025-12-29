package ie.setu.appstore.models

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import ie.setu.appstore.helpers.exists
import ie.setu.appstore.helpers.read
import ie.setu.appstore.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Random
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class AppCloudStore(private val context: Context) : AppStore {

    var apps = mutableListOf<AppModel>()
    lateinit var database: DatabaseReference

    init {
        database = Firebase.database("https://genuine-cat-482020-s4-default-rtdb.europe-west1.firebasedatabase.app/").reference
        deserialize()
//        if (exists(context, JSON_FILE)) {
//            deserialize()
//        }
    }

    override fun findAll(): MutableList<AppModel> {
        logAll()
        return apps
    }

    override fun create(app: AppModel) {
        app.id = generateRandomId()
        apps.add(app)
        serialize()
    }

    override fun update(app: AppModel) {
        var foundApp: AppModel? = apps.find { a -> a.id == app.id }
        if (foundApp != null) {
            foundApp.name = app.name
            foundApp.appType = app.appType
            foundApp.price = app.price
            foundApp.ratings = app.ratings
            foundApp.icon = app.icon
            logAll()
        }
        serialize()
    }

    override fun delete(id: Int) {
        var foundApp: AppModel? = apps.find { a -> a.id == id }
        if (foundApp != null) {
            apps.remove(foundApp)
            logAll()
        }
        serialize()
    }

    override fun delete(app: AppModel) {
        apps.remove(app)
        serialize()
    }

    override fun search(query: String): List<AppModel> {
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

    override fun filter(p: (AppModel) -> Boolean): List<AppModel> {
        return ArrayList(apps.filter(p))
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(apps, listType)
//        write(context, JSON_FILE, jsonString)
        val ref = database.child("appstore")
        ref.setValue(jsonString)
    }

    private fun deserialize() {
        database.child("appstore").get().addOnSuccessListener {
            val jsonString = it.value as String?
            if (jsonString != null) {
                apps = gsonBuilder.fromJson(jsonString, listType)
            }
        }
//        val jsonString = read(context, JSON_FILE)
    }

    private fun logAll() {
        apps.forEach { Timber.i("$it") }
    }
}

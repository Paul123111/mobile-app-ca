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

const val JSON_FILE = "placemarks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<AppModel>>() {}.type

fun generateRandomId(): Int {
    return Random().nextInt()
}

class AppJSONStoreV2(private val context: Context) : AppStore {

    var apps = mutableListOf<AppModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
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
        // todo
    }

    override fun delete(id: Int) {
        // todo
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
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        apps = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        apps.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.toString())
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
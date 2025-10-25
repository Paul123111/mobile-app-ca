package ie.setu.appstore.models

import android.content.Context

interface JsonStore<T> {
    var context: Context
    fun loadFromFile()
    fun readFromFile(): List<T>
    fun writeToFile()
    fun setJsonContext(context: Context)
}
package ie.setu.appstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AppModel(var id: Int = 0,
                    var name: String = "",
                    var appType: AppType = AppType.App) : Parcelable {
    enum class AppType(val value: Int) {App(0), Game(1), Hi(2)} }
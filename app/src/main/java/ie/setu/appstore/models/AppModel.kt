package ie.setu.appstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppModel(var id: Long = 0,
                    var name: String = "") : Parcelable
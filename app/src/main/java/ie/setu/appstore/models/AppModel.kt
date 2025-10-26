package ie.setu.appstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Parcelize
@Serializable
data class AppModel(var id: Int = 0,
                    var name: String = "",
                    var appType: AppType = AppType.App,
                    var price: Int = 1 ) : Parcelable {
    enum class AppType(val value: Int) {App(0), Game(1), Hi(2)}

    fun priceToString(): String {
        var priceDigits = price.toString().map { it.toString() }
        while (priceDigits.size < 3) {
            priceDigits = listOf("0").plus(priceDigits)
        }
        return "€".plus(priceDigits.dropLast(2).joinToString(""))
            .plus(".")
            .plus(priceDigits.takeLast(2).joinToString(""))
    }
    }
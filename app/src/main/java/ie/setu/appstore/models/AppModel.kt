package ie.setu.appstore.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppModel(var id: Int = 0,
                    var name: String = "",
                    var appType: AppType = AppType.App,
                    var price: Int = 1,
                    var ratings: ArrayList<CommentModel> = ArrayList(),
                    var icon: Uri = Uri.EMPTY
) : Parcelable {
    enum class AppType(val value: Int) {App(0), Game(1)}

    fun addRating(rating: CommentModel) {
        ratings.add(rating)
    }

    fun avgRating(): Float {
        if (ratings.isEmpty()) {return 0F}
        var sum = 0F
        for (rating in ratings) {
            sum += rating.rating
        }
        return sum/ratings.size
    }

    fun priceToString(): String {
        if (price == 0) {
            return "Free"
        }

        var priceDigits = price.toString().map { it.toString() }
        while (priceDigits.size < 3) {
            priceDigits = listOf("0").plus(priceDigits)
        }
        return "€".plus(priceDigits.dropLast(2).joinToString(""))
            .plus(".")
            .plus(priceDigits.takeLast(2).joinToString(""))
        }
    }

@Parcelize
data class CommentModel(
    val rating: Int,
    val username: String,
    val comment: String
) : Parcelable
{}

package ie.setu.appstore.views.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber.i

class AppstoreAddPresenter(private val view: AppstoreAddView) {

    var app = AppModel()
    var mainApp = view.application as MainApp
    var edit = false
    private lateinit var imageIntentLauncher : ActivityResultLauncher<PickVisualMediaRequest>

    init {
        if (view.intent.hasExtra("app_edit")) {
            // getParcelable new method not available in Android 30
            app = view.intent.extras?.getParcelable("app_edit")!!
            edit = true
        }
        registerImagePickerCallback()
    }

    fun addOrSave(name: String, type: AppModel.AppType, price: Int) {
        app.name = name
        app.appType = type
        app.price = price
        if (edit) {
            mainApp.apps.update(app.copy())
        } else {
            mainApp.apps.create(app.copy())
        }
        view.setResult(RESULT_OK)
        view.finish()
    }

    fun cancel() {
        view.finish()
    }

    fun delete() {
        view.setResult(99)
        mainApp.apps.delete(app.id)
        view.finish()
    }

    fun cacheApp(name: String, type: AppModel.AppType, price: Int) {
        app.name = name
        app.appType = type
        app.price = price
    }

    fun selectImage() {
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        imageIntentLauncher.launch(request)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = view.registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {
            try{
                view.contentResolver
                    .takePersistableUriPermission(it!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION )
                app.icon = it // The returned Uri
                i("IMG :: ${app.icon}")
                view.updateIcon(app.icon)
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }
    }

}
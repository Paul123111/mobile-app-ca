package ie.setu.appstore.views.add

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import ie.setu.appstore.R
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber.i

class AppstoreAddPresenter(private val view: AppAddFragment) {

    var app = AppModel()
    private lateinit var mainApp: MainApp
    var edit = false
    private lateinit var imageIntentLauncher : ActivityResultLauncher<PickVisualMediaRequest>

    init {
        mainApp = view.activity?.application as MainApp
        val bundle = view.arguments
        if (bundle?.containsKey("app_edit") == true) {
            app = bundle.getParcelable<AppModel>("app_edit")!!
            view.showApp(app)
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
//        view.activity.setResult(RESULT_OK)
//        view.finish()
        view.findNavController().navigate(R.id.action_appAddFragment_to_homeViewFragment)
    }

    fun cancel() {
//        view.finish()
        view.findNavController().navigate(R.id.action_appAddFragment_to_homeViewFragment)
    }

    fun delete() {
//        view.setResult(99)
        mainApp.apps.delete(app.id)
//        view.finish()
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
                view.activity?.contentResolver
                    ?.takePersistableUriPermission(it!!,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION )
                app.icon = it!! // The returned Uri
                i("IMG :: ${app.icon}")
                view.updateIcon(app.icon)
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }
    }

}
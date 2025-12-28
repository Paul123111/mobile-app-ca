package ie.setu.appstore.views.view

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import ie.setu.appstore.R
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel

class AppViewPresenter(val view: AppViewFragment) {
    lateinit var mainApp: MainApp
    var app = AppModel()

    init {
        mainApp = view.activity?.application as MainApp
        val bundle = view.arguments
        if (bundle?.containsKey("app_edit") == true) {
            app = bundle.getParcelable<AppModel>("app_edit")!!
            view.updateView(app)
        }
    }

    fun addRating(rating: Int) {
        app.addRating(rating)
        mainApp.apps.update(app)
        view.updateView(app)
    }

    fun editApp() {
        val args = Bundle()
        args.putParcelable("app_edit", app)
        view.findNavController().navigate(R.id.action_appViewFragment_to_appAddFragment, args)
    }

}
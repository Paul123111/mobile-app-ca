package ie.setu.appstore.views.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import ie.setu.appstore.R
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel

class AppstoreHomePresenter(val view: HomeViewFragment) {
    lateinit var mainApp: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        mainApp = view.activity?.application as MainApp
        registerRefreshCallback()
    }

    fun getAllApps() = mainApp.apps.findAll()
    fun getGames() = mainApp.apps.findAll().filter { a -> (a.appType == AppModel.AppType.Game) }
    fun getApps() = mainApp.apps.findAll().filter { a -> (a.appType == AppModel.AppType.App) }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) {
                    view.onRefresh()
                } else if (it.resultCode == 99) {
                    view.onDelete(position)
                }
            }
    }

    fun addApp() {
//        val launcherIntent = Intent(view, AppstoreAddView::class.java)
//        refreshIntentLauncher.launch(launcherIntent)
        view.findNavController().navigate(R.id.action_homeViewFragment_to_appAddFragment)
    }

    fun searchApps() {
        view.findNavController().navigate(R.id.action_homeViewFragment_to_appSearchFragment)
    }

    fun editApp(app: AppModel, pos: Int) {
//        val launcherIntent = Intent(view, AppViewActivity::class.java)
//        launcherIntent.putExtra("app_edit", app)
//        position = pos
//        refreshIntentLauncher.launch(launcherIntent)
        val args = Bundle()
        args.putParcelable("app_edit", app)
        view.findNavController().navigate(R.id.action_homeViewFragment_to_appViewFragment, args)
    }
}
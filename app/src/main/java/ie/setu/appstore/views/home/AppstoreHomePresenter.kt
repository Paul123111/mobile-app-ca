package ie.setu.appstore.views.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.appstore.activities.AppViewActivity
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import ie.setu.appstore.views.add.AppstoreAddView

class AppstoreHomePresenter(val view: AppstoreHomeView) {
    lateinit var mainApp: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        mainApp = view.application as MainApp
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

    fun addPlacemark() {
        val launcherIntent = Intent(view, AppstoreAddView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun editPlacemark(app: AppModel, pos: Int) {
        val launcherIntent = Intent(view, AppViewActivity::class.java)
        launcherIntent.putExtra("app_edit", app)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }
}
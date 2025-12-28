package ie.setu.appstore.views.search

import androidx.core.util.Predicate
import androidx.navigation.fragment.findNavController
import ie.setu.appstore.R
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel

class AppstoreSearchPresenter(private val view: AppSearchFragment) {
    lateinit var mainApp: MainApp

    init {
        mainApp = view.activity?.application as MainApp
    }

    fun getAllApps() = mainApp.apps.findAll()

    fun sort(apps: List<AppModel>, c: Comparator<AppModel>) = apps.sortedWith(c)

    fun filter(apps: List<AppModel>, p: (AppModel) -> Boolean) = apps.filter(p)

    fun search(str: String) = mainApp.apps.search(str)

    fun addApp() {
        view.findNavController().navigate(R.id.action_appSearchFragment_to_appAddFragment)
    }

    fun home() {
        view.findNavController().navigate(R.id.action_appSearchFragment_to_homeViewFragment)
    }
}

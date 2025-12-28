package ie.setu.appstore.views.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.adapter.AppHomeAdapter
import ie.setu.appstore.adapter.AppListener
import ie.setu.appstore.databinding.FragmentHomeBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber.i

class HomeViewFragment: Fragment(R.layout.fragment_home), AppListener {
    private var binding: FragmentHomeBinding? = null
    lateinit var mainApp: MainApp
    lateinit var presenter: AppstoreHomePresenter
    private var position: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        presenter = AppstoreHomePresenter(this)
        mainApp = activity?.application as MainApp

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.appsView?.layoutManager = layoutManager
        val layoutManager2 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.gamesView?.layoutManager = layoutManager2
        loadApps()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

    fun onRefresh() {
        binding?.appsView?.swapAdapter(AppHomeAdapter(presenter.getApps(), this), true)
        binding?.gamesView?.swapAdapter(AppHomeAdapter(presenter.getGames(), this), true)
    }

    fun onDelete(position: Int) {
        binding?.appsView?.adapter?.notifyItemRemoved(position)
        binding?.gamesView?.adapter?.notifyItemRemoved(position)
    }

    private fun loadApps() {
        binding?.appsView?.adapter = AppHomeAdapter(presenter.getApps(), this)
        binding?.gamesView?.adapter = AppHomeAdapter(presenter.getGames(), this)
        onRefresh()
    }

    override fun onAppClick(app: AppModel, position: Int) {
        this.position = position
        onRefresh()
        presenter.editPlacemark(app, this.position)
    }
}
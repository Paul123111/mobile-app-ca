package ie.setu.appstore.views.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.activities.AppstoreActivity
import ie.setu.appstore.activities.AppstoreAddActivity
import ie.setu.appstore.adapter.AppHomeAdapter
import ie.setu.appstore.adapter.AppListener
import ie.setu.appstore.adapter.AppstoreAdapter
import ie.setu.appstore.databinding.ActivityAppstoreHomeBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import ie.setu.appstore.views.add.AppstoreAddView
import timber.log.Timber
import timber.log.Timber.i

class AppstoreHomeView : AppCompatActivity(), AppListener {
    private lateinit var binding: ActivityAppstoreHomeBinding
    lateinit var mainApp: MainApp
    lateinit var presenter: AppstoreHomePresenter
    private var position: Int = 0
    private var appType: AppModel.AppType = AppModel.AppType.App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        presenter = AppstoreHomePresenter(this)

        binding = ActivityAppstoreHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Appstore home view started")

        mainApp = application as MainApp

        binding.bottomNavigationView.setOnItemSelectedListener{item -> (
                when (item.itemId) {
                    R.id.item_add -> { presenter.addPlacemark() }
                    R.id.item_search -> {
                        val launcherIntent = Intent(this, AppstoreActivity::class.java)
                        getResult.launch(launcherIntent)
                    }
                    R.id.item_home -> {
                        onRefresh()
                    }
                    else -> i("unknown option")
                })
            return@setOnItemSelectedListener true
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.appsView.layoutManager = layoutManager
        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.gamesView.layoutManager = layoutManager2
        loadApps()
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.appsView.adapter)?.
                notifyItemRangeChanged(0,presenter.getApps().size)
                (binding.gamesView.adapter)?.
                notifyItemRangeChanged(0, presenter.getGames().size)
            }
        }

    override fun onAppClick(app: AppModel, position: Int) {
        this.position = position
        onRefresh()
        presenter.editPlacemark(app, this.position)
    }

    fun onRefresh() {
        i("refresh")
//        (binding.appsView.adapter)?.
//        notifyItemRangeChanged(0,presenter.getApps().size+1)
//        (binding.gamesView.adapter)?.
//        notifyItemRangeChanged(0, presenter.getGames().size+1)
//        binding.appsView.adapter.let {it as AppHomeAdapter}.filterApps()
//        (binding.appsView.adapter)?.notifyDataSetChanged()
//        binding.gamesView.adapter.let {it as AppHomeAdapter}.filterGames()
//        (binding.gamesView.adapter)?.notifyDataSetChanged()
        binding.appsView.swapAdapter(AppHomeAdapter(presenter.getApps(), this), true)
        binding.gamesView.swapAdapter(AppHomeAdapter(presenter.getGames(), this), true)
    }

    fun onDelete(position: Int) {
        binding.appsView.adapter?.notifyItemRemoved(position)
        binding.gamesView.adapter?.notifyItemRemoved(position)
    }

    private fun loadApps() {
        binding.appsView.adapter = AppHomeAdapter(presenter.getApps(), this)
        binding.gamesView.adapter = AppHomeAdapter(presenter.getGames(), this)
        onRefresh()
    }

}
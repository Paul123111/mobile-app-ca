package ie.setu.appstore.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.adapter.AppstoreAdapter
import ie.setu.appstore.databinding.ActivityAppstoreBinding
import ie.setu.appstore.main.MainApp
import timber.log.Timber
import timber.log.Timber.i

class AppstoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppstoreBinding
    lateinit var mainApp: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppstoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Appstore activity started")

        mainApp = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = AppstoreAdapter(mainApp.apps)

        binding.bottomNavigationView.setOnItemSelectedListener{item -> (
            when (item.itemId) {
                R.id.item_add -> {
                    val launcherIntent = Intent(this, AppstoreAddActivity::class.java)
                    getResult.launch(launcherIntent)
                }
                else -> i("unknown option")
            })
            return@setOnItemSelectedListener true
            }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
            }
        }
}
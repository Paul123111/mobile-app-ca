package ie.setu.appstore.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.adapter.AppListener
import ie.setu.appstore.adapter.AppstoreAdapter
import ie.setu.appstore.databinding.ActivityAppstoreBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber
import timber.log.Timber.i
import java.io.File

class AppstoreActivity : AppCompatActivity(), AppListener {

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
        binding.recyclerView.adapter = AppstoreAdapter(mainApp.apps.findAll(), this)

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

        // if search not empty
        binding.appSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                i("searching for ${binding.appSearch.text}")
                val searchAppList = mainApp.apps.search(binding.appSearch.text.toString())
                binding.recyclerView.swapAdapter(AppstoreAdapter(searchAppList, this@AppstoreActivity), true)
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
            }
        })

    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                binding.appSearch.setText("")
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
            }
        }

    override fun onAppClick(app: AppModel) {
        val launcherIntent = Intent(this, AppstoreAddActivity::class.java)
        launcherIntent.putExtra("app_edit", app)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                binding.appSearch.setText("")
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
                if (mainApp.apps.lastRemovedId != -1) (binding.recyclerView.adapter)?.notifyItemRemoved(mainApp.apps.lastRemovedId)
                mainApp.apps.lastRemovedId = -1
            }
        }
}
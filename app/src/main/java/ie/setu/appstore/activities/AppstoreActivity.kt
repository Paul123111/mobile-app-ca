package ie.setu.appstore.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityAppstoreBinding
import ie.setu.appstore.main.MainApp
import timber.log.Timber
import timber.log.Timber.i

class AppstoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppstoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppstoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Appstore activity started")

        //binding.toolbar.title = title
        //setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_hotbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, AppstoreAddActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
        }
}
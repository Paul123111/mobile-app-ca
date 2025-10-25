package ie.setu.appstore.activities

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityAppstoreBinding

class AppstoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppstoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppstoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_hotbar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
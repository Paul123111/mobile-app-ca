package ie.setu.appstore.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityAppstoreAddBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber
import timber.log.Timber.i

class AppstoreAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppstoreAddBinding
    var app = AppModel()
    lateinit var mainApp: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppstoreAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Placemark Activity started...")

        mainApp = application as MainApp

        if (intent.hasExtra("app_edit")) {
            app = intent.extras?.getParcelable("app_edit")!!
            binding.placemarkTitle.setText(app.name)
        }

        binding.btnAdd.setOnClickListener() {
            app.name = binding.placemarkTitle.text.toString()
            if (app.name.isNotEmpty()) {
                mainApp.apps.create(app)
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
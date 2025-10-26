package ie.setu.appstore.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

        // https://stackoverflow.com/questions/68282173/convert-enum-values-to-arraystring-with-a-generic-function-in-kotlin
        // so I don't have to maintain array string based on enum
        val appTypes = AppModel.AppType.entries.map{it.name}
        binding.appType.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, appTypes)

        var edit: Boolean = false

        if (intent.hasExtra("app_edit")) {
            app = intent.extras?.getParcelable("app_edit")!!
            binding.appName.setText(app.name)
            binding.appType.setSelection(app.appType.value)
            edit = true
        }

        if (edit) {
            binding.btnAdd.setText(R.string.button_appEdit)
            // delete only visible in edit mode
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.btnAdd.setText(R.string.button_appAdd)
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnDelete.setOnClickListener {
            mainApp.apps.delete(app.id)
            setResult(RESULT_OK)
            finish()
        }

        binding.btnAdd.setOnClickListener() {
            app.name = binding.appName.text.toString()
            app.appType = AppModel.AppType.valueOf(binding.appType.selectedItem.toString())
            if (app.name.isNotEmpty()) {
                if (edit) {
                    mainApp.apps.update(app.copy())
                } else {
                    mainApp.apps.create(app.copy())
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
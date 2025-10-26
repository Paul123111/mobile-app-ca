package ie.setu.appstore.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityAppstoreAddBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import ie.setu.appstore.utils.DecimalDigitsInputFilter
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
        // just line 37, so I don't have to maintain array string based on enum
        val appTypes = AppModel.AppType.entries.map{it.name}
        binding.appType.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, appTypes)

        binding.appPrice.filters = arrayOf(DecimalDigitsInputFilter(3, 2))

        var edit = false

        if (intent.hasExtra("app_edit")) {
            // getParcelable new method not available in Android 30
            app = intent.extras?.getParcelable("app_edit")!!
            binding.appName.setText(app.name)
            binding.appType.setSelection(app.appType.value)
            val decimalPrice = app.price/100F
            binding.appPrice.setText(decimalPrice.toString())
            edit = true
        }

        if (edit) {
            binding.btnAdd.setText(R.string.button_appEdit)
            // delete only visible in edit mode
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.btnAdd.setText(R.string.button_appAdd)
            binding.btnDelete.visibility = View.GONE
            binding.appPrice.setText(R.string.editText_priceDefault)
        }

        binding.btnDelete.setOnClickListener {
            mainApp.apps.delete(app.id)
            setResult(RESULT_OK)
            finish()
        }

        binding.btnAdd.setOnClickListener {
            app.name = binding.appName.text.toString()
            app.appType = AppModel.AppType.valueOf(binding.appType.selectedItem.toString())
            app.price = (binding.appPrice.text.toString().toFloat()*100).toInt()
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
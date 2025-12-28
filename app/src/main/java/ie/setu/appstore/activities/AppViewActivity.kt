package ie.setu.appstore.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.ArrayAdapter
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.android.material.snackbar.Snackbar
//import ie.setu.appstore.R
//import ie.setu.appstore.adapter.AppstoreAdapter
//import ie.setu.appstore.adapter.RatingAdapter
//import ie.setu.appstore.databinding.ActivityAppViewBinding
//import ie.setu.appstore.databinding.ActivityAppstoreAddBinding
//import ie.setu.appstore.main.MainApp
//import ie.setu.appstore.models.AppModel
//import ie.setu.appstore.utils.DecimalDigitsInputFilter
//import ie.setu.appstore.views.add.AppstoreAddView
//import timber.log.Timber
//import timber.log.Timber.i
//
//class AppViewActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityAppViewBinding
//    var app = AppModel()
//    lateinit var mainApp: MainApp
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAppViewBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        Timber.plant(Timber.DebugTree())
//        i("Placemark Activity started...")
//
//        mainApp = application as MainApp
//
//        binding.rating.setMaxValue(5)
//        binding.rating.setMinValue(1)
//
//        if (intent.hasExtra("app_edit")) {
//            // getParcelable new method not available in Android 30
//            app = intent.extras?.getParcelable("app_edit")!!
//            binding.appName.setText(app.name)
//            binding.appType.setText(app.appType.toString())
//            binding.appPrice.setText(app.priceToString())
//        }
//
//        binding.avgRating.text = app.avgRating().toString()
//
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.recyclerView.layoutManager = layoutManager
//        val adapter = RatingAdapter(app.ratings)
//        binding.recyclerView.adapter = adapter
//
//        binding.btnEdit.setOnClickListener {
//            val launcherIntent = Intent(this, AppstoreAddView::class.java)
//            launcherIntent.putExtra("app_edit", app)
//            getResult.launch(launcherIntent)
//            setResult(RESULT_OK)
//            finish()
//        }
//
//        binding.btnRating.setOnClickListener {
//            app.addRating(binding.rating.value)
//            mainApp.apps.update(app)
//            binding.avgRating.text = app.avgRating().toString()
//            binding.recyclerView.adapter?.notifyItemRangeChanged(0, app.ratings.size)
//        }
//    }
//
//    private val getResult =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//            if (it.resultCode == RESULT_OK) {
//
//            }
//        }
//
//
//}
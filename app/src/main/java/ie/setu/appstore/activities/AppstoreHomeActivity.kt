package ie.setu.appstore.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import ie.setu.appstore.R
import ie.setu.appstore.adapter.AppHomeAdapter
import ie.setu.appstore.adapter.AppListener
import ie.setu.appstore.adapter.AppstoreAdapter
import ie.setu.appstore.databinding.ActivityAppstoreBinding
import ie.setu.appstore.databinding.ActivityAppstoreHomeBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber
import timber.log.Timber.i

class AppstoreHomeActivity : AppCompatActivity(), AppListener {
    private lateinit var binding: ActivityAppstoreHomeBinding
    lateinit var mainApp: MainApp
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityAppstoreHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Appstore home activity started")

        mainApp = application as MainApp

        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(getString(R.string.default_web_client_id))
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(true)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        binding.bottomNavigationView.setOnItemSelectedListener{item -> (
                when (item.itemId) {
                    R.id.item_add -> {
                        val launcherIntent = Intent(this, AppstoreAddActivity::class.java)
                        getResult.launch(launcherIntent)
                    }
                    R.id.item_search -> {
                        val launcherIntent = Intent(this, AppstoreActivity::class.java)
                        getResult.launch(launcherIntent)
                    }
                    R.id.item_home -> {
                    }
                    else -> i("unknown option")
                })
            return@setOnItemSelectedListener true
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.appsView.layoutManager = layoutManager
        val adapter = AppHomeAdapter(
            mainApp.apps.findAll().filter { a -> (a.appType == AppModel.AppType.App) }, this
        )
        binding.appsView.adapter = adapter

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.gamesView.layoutManager = layoutManager2
        val adapter2 = AppHomeAdapter(mainApp.apps.findAll().filter { a -> (a.appType == AppModel.AppType.Game) }, this)
        binding.gamesView.adapter = adapter2
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.appsView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
                (binding.gamesView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
            }
        }

    override fun onAppClick(app: AppModel) {
        val launcherIntent = Intent(this, AppViewActivity::class.java)
        launcherIntent.putExtra("app_edit", app)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.appsView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
                (binding.gamesView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
            }
        }
}
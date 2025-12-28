package ie.setu.appstore.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityAppstoreHomeBinding
import ie.setu.appstore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
//        binding = DataBindingUtil.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
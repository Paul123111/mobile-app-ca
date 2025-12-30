package ie.setu.appstore.activities

import android.os.Bundle
import android.util.Log.i
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber.i

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        firebaseAuth.signOut()
        updateDrawer()

        binding.drawer.drawer.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_logout -> logOut()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseAuth.signOut()
    }

    fun updateDrawer() {
        val drawer = binding.drawer.root
        val header = drawer.getHeaderView(0)
        val text = header.findViewById<TextView>(R.id.textView)
        val panel = header.findViewById<ImageView>(R.id.drawerImageView)
        if (firebaseAuth.currentUser != null) {
            text.text = firebaseAuth.currentUser!!.email
            Picasso.get()
                .load(R.mipmap.ic_launcher)
                .into(panel)
        } else {
            text.text = "Not logged in"
            Picasso.get()
                .load(R.mipmap.ic_launcher)
                .into(panel!!)
        }
    }

    fun logOut() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            updateDrawer()
            findNavController(R.id.fragmentContainerView).popBackStack(R.id.loginFragment, false)
        }
    }
}
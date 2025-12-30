package ie.setu.appstore.activities

import android.os.Bundle
import android.util.Log.i
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber.i

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mMap: GoogleMap
    var dragMap: Boolean = false

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val drawer = binding.drawer.root
//        val header = drawer.getHeaderView(0)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCameraMoveStarted(p0: Int) {
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
    }

    override fun onCameraIdle() {
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}
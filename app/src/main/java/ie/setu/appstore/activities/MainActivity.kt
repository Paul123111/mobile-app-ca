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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.squareup.picasso.Picasso
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityMainBinding
import ie.setu.appstore.models.Location
import ie.setu.appstore.models.gsonBuilder
import ie.setu.appstore.models.listType
import ie.setu.appstore.models.locationType
import timber.log.Timber
import timber.log.Timber.i
import java.util.HashMap

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerDragListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mMap: GoogleMap
    var dragMap: Boolean = false
    var currentMarker: Marker? = null
    var currentLocation: Location? = null
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.database("https://genuine-cat-482020-s4-default-rtdb.europe-west1.firebasedatabase.app/").reference
        setContentView(binding.root)
        firebaseAuth.signOut()
        updateDrawer()

        binding.drawer.drawer.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_logout -> logOut()
            }
            true
        }

        binding.backBtn.setOnClickListener {
            moveBack()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val drawer = binding.drawer.root
//        val header = drawer.getHeaderView(0)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onPause() {
        super.onPause()
        logOut()
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
            getMarker()
        } else {
            text.text = "Not logged in"
            Picasso.get()
                .load(R.mipmap.ic_launcher)
                .into(panel!!)
            currentMarker?.remove()
        }
    }

    fun logOut() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
            updateDrawer()
            findNavController(R.id.fragmentContainerView).popBackStack(R.id.loginFragment, false)
        }
    }

    fun moveBack() {
        findNavController(R.id.fragmentContainerView).navigateUp()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerDragListener(this)
        updateMarker()
    }

    fun updateMarker() {
        currentMarker?.remove()
        if (firebaseAuth.currentUser != null) {
            val latLng = LatLng(currentLocation!!.lat, currentLocation!!.lng)
            val options = MarkerOptions()
                .title("User Location")
                .draggable(true)
                .position(latLng)
            currentMarker = mMap.addMarker(options)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentLocation!!.zoom))
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    override fun onCameraMoveStarted(p0: Int) {
        if (!dragMap) {
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
        }
    }

    override fun onCameraIdle() {
        if (!dragMap) {
            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//            binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)

        }
    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(p0: Marker) {
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        dragMap = false
        saveMarker(p0)
    }

    override fun onMarkerDragStart(p0: Marker) {
        dragMap = true
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
    }

    fun saveMarker(p0: Marker) {
        if (firebaseAuth.currentUser != null) {
            val ref =
                database.child("users").child(firebaseAuth.currentUser!!.uid).child("location")
            val newLocation =
                Location(p0.position.latitude, p0.position.longitude, mMap.cameraPosition.zoom)
            val jsonString = gsonBuilder.toJson(newLocation, locationType)
            ref.setValue(jsonString)
        }
    }

    fun getMarker() {
        currentLocation = Location(52.245696, -7.139102, 7f)
        val ref =
            database.child("users").child(firebaseAuth.currentUser!!.uid)
        ref.get().addOnSuccessListener {
            val json = it.value as HashMap<String, String>?
            if (json != null) {
                val storedLoc: String? = json["location"]
                currentLocation = gsonBuilder.fromJson(storedLoc, locationType)
            }
            updateMarker()
        }.addOnFailureListener {
            updateMarker()
        }
    }
}

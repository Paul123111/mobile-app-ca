package ie.setu.appstore.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.adapter.AppListener
import ie.setu.appstore.adapter.AppstoreAdapter
import ie.setu.appstore.databinding.ActivityAppstoreBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import ie.setu.appstore.views.add.AppstoreAddView
import ie.setu.appstore.views.home.AppstoreHomeView
import timber.log.Timber
import timber.log.Timber.i
import java.util.ArrayList

class AppstoreActivity : AppCompatActivity(), AppListener {

    private lateinit var binding: ActivityAppstoreBinding
    lateinit var mainApp: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppstoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Appstore activity started")

        mainApp = application as MainApp

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = AppstoreAdapter(mainApp.apps.findAll(), this)
        binding.recyclerView.adapter = adapter

        val appTypes = AppModel.AppType.entries.map{it.name}.plus("All")
        binding.appTypeFilter.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, appTypes)

        val appSort = resources.getStringArray(R.array.spinner_sort)
        binding.appPriceFilter.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, appSort)

        binding.bottomNavigationView.setOnItemSelectedListener{item -> (
            when (item.itemId) {
                R.id.item_add -> {
                    finish()
                    val launcherIntent = Intent(this, AppstoreAddView::class.java)
                    getResult.launch(launcherIntent)
                }
                R.id.item_search -> {
                }
                R.id.item_home -> {
                    finish()
                    val launcherIntent = Intent(this, AppstoreHomeView::class.java)
                    getResult.launch(launcherIntent)
                }
                else -> i("unknown option")
            })
            return@setOnItemSelectedListener true
        }

        binding.appPriceFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(binding.appPriceFilter.selectedItemPosition) {
                    0 -> {
                        mainApp.apps.sort { a, b -> (a.name.compareTo(b.name)) }
                    }
                    1 -> {
                        mainApp.apps.sort { a, b -> (b.name.compareTo(a.name)) }
                    }
                    2 -> {
                        mainApp.apps.sort { a, b -> (a.price - b.price).compareTo(0) }
                    }
                    3 -> {
                        mainApp.apps.sort { a, b -> (b.price - a.price).compareTo(0) }
                    }
                }
                search()
            }

        }

        binding.appTypeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                search()
            }

        }

        // if search not empty
        binding.appSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener{

            override fun onQueryTextChange(newText: String?): Boolean {
                search()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                binding.appSearch.setQuery("", false)
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
                search()
            }
        }

    override fun onAppClick(app: AppModel, position: Int) {
        val launcherIntent = Intent(this, AppViewActivity::class.java)
        launcherIntent.putExtra("app_edit", app)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                binding.appSearch.setQuery("", false)
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,mainApp.apps.findAll().size)
//                if (mainApp.apps.lastRemovedId != -1) (binding.recyclerView.adapter)?.notifyItemRemoved(mainApp.apps.lastRemovedId)
//                mainApp.apps.lastRemovedId = -1
                binding.recyclerView.adapter?.notifyDataSetChanged()
                search()
            }
        }

    fun search() {
        var searchAppList = mainApp.apps.search(binding.appSearch.query.toString())

        when(binding.appTypeFilter.selectedItemPosition) {
            0 -> {
                searchAppList = ArrayList(searchAppList.filter { a -> (a.appType == AppModel.AppType.App) })
            }
            1 -> {
                searchAppList = ArrayList(searchAppList.filter { a -> (a.appType == AppModel.AppType.Game) })
            }
            else -> {}
        }
        binding.recyclerView.swapAdapter(AppstoreAdapter(searchAppList, this@AppstoreActivity), true)
        (binding.recyclerView.adapter)?.
        notifyItemRangeChanged(0,mainApp.apps.findAll().size)
    }
}
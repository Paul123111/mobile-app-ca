package ie.setu.appstore.views.search

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.adapter.AppListener
import ie.setu.appstore.adapter.AppstoreAdapter
import ie.setu.appstore.databinding.ActivityAppstoreBinding
import ie.setu.appstore.main.MainApp
import ie.setu.appstore.models.AppModel
import timber.log.Timber
import timber.log.Timber.i
import java.util.ArrayList

class AppSearchFragment: Fragment(R.layout.activity_appstore), AppListener {
    private lateinit var binding: ActivityAppstoreBinding
    private lateinit var presenter: AppstoreSearchPresenter
    private lateinit var appList: ArrayList<AppModel>
    private var position: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityAppstoreBinding.bind(view)
        presenter = AppstoreSearchPresenter(this)

        val layoutManager = LinearLayoutManager(this.requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = AppstoreAdapter(presenter.getAllApps(), this)
        binding.recyclerView.adapter = adapter

        val appTypes = AppModel.AppType.entries.map{it.name}.plus("All")
        binding.appTypeFilter.adapter = ArrayAdapter(this.requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, appTypes)

        val appSort = resources.getStringArray(R.array.spinner_sort)
        binding.appPriceFilter.adapter = ArrayAdapter(this.requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, appSort)

        binding.bottomNavigationView.setOnItemSelectedListener{item -> (
                when (item.itemId) {
                    R.id.item_add -> {
                        presenter.addApp()
                    }
                    R.id.item_search -> {
                    }
                    R.id.item_home -> {
                        presenter.home()
                    }
                    else -> i("unknown option")
                })
            return@setOnItemSelectedListener true
        }

        binding.appPriceFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sort()
            }

        }

        binding.appTypeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                search()
                sort()
            }

        }

        // if search not empty
        binding.appSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener{

            override fun onQueryTextChange(newText: String?): Boolean {
                search()
                sort()
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
                notifyItemRangeChanged(0,appList.size)
                search()
            }
        }

    override fun onAppClick(app: AppModel, position: Int) {
//        val launcherIntent = Intent(this, AppViewActivity::class.java)
//        launcherIntent.putExtra("app_edit", app)
//        getClickResult.launch(launcherIntent)
        this.position = position
        onRefresh()
        presenter.editApp(app, this.position)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                binding.appSearch.setQuery("", false)
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,appList.size)
//                if (mainApp.apps.lastRemovedId != -1) (binding.recyclerView.adapter)?.notifyItemRemoved(mainApp.apps.lastRemovedId)
//                mainApp.apps.lastRemovedId = -1
                binding.recyclerView.adapter?.notifyDataSetChanged()
                search()
            }
        }

    fun search() {
        appList = presenter.search(binding.appSearch.query.toString()) as ArrayList<AppModel>

        when(binding.appTypeFilter.selectedItemPosition) {
            0 -> {
                appList = presenter.filter(appList, { a -> (a.appType == AppModel.AppType.App) }) as ArrayList<AppModel>
            }
            1 -> {
                appList = presenter.filter(appList, { a -> (a.appType == AppModel.AppType.Game) }) as ArrayList<AppModel>
            }
            else -> {}
        }
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.swapAdapter(AppstoreAdapter(appList, this), true)
        (binding.recyclerView.adapter)?.
        notifyItemRangeChanged(0,appList.size)
    }

    fun sort() {
        when(binding.appPriceFilter.selectedItemPosition) {
            0 -> {
                appList = ArrayList(
                    presenter.sort(
                        appList,
                        { a, b -> (a.name.lowercase().compareTo(b.name.lowercase())) })
                )
            }

            1 -> {
                appList = ArrayList(
                    presenter.sort(
                        appList,
                        { a, b -> (b.name.lowercase().compareTo(a.name.lowercase())) })
                )
            }

            2 -> {
                appList = ArrayList(
                    presenter.sort(
                        appList,
                        { a, b -> (a.price - b.price).compareTo(0) })
                )
            }

            3 -> {
                appList = ArrayList(
                    presenter.sort(
                        appList,
                        { a, b -> (b.price - a.price).compareTo(0) })
                )
            }
        }
        onRefresh()
    }
}
package ie.setu.appstore.views.add

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.appstore.R
import ie.setu.appstore.databinding.ActivityAppstoreAddBinding
import ie.setu.appstore.databinding.FragmentAppAddBinding
import ie.setu.appstore.models.AppModel
import ie.setu.appstore.utils.DecimalDigitsInputFilter
import timber.log.Timber
import timber.log.Timber.i

class AppAddFragment: Fragment(R.layout.fragment_app_add) {
    private lateinit var binding: FragmentAppAddBinding
    private lateinit var presenter: AppstoreAddPresenter
    var app = AppModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAppAddBinding.inflate(layoutInflater)
        presenter = AppstoreAddPresenter(this)

        // https://stackoverflow.com/questions/68282173/convert-enum-values-to-arraystring-with-a-generic-function-in-kotlin
        // just line 37, so I don't have to maintain array string based on enum (for dropdown)
        val appTypes = AppModel.AppType.entries.map{it.name}
        binding.appType.adapter = ArrayAdapter(requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, appTypes)
        // for price numberPicker
        binding.appPrice.filters = arrayOf(DecimalDigitsInputFilter(3, 2))

        binding.btnCancel.setOnClickListener {
            presenter.cancel()
        }

        binding.btnDelete.setOnClickListener {
            presenter.delete()
        }
        binding.btnDelete.visibility = if (presenter.edit) View.VISIBLE else View.GONE

        binding.btnAdd.setOnClickListener {
            if (binding.appName.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.editText_appName, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.addOrSave(
                    binding.appName.text.toString(),
                    AppModel.AppType.valueOf(binding.appType.selectedItem.toString()),
                    (binding.appPrice.text.toString().toFloat()*100).toInt()
                )
            }
        }
        binding.btnAdd.setText(if (!presenter.edit) R.string.button_appAdd else R.string.button_appEdit)

        binding.chooseImage.setOnClickListener {
//            presenter.cacheApp(
//                binding.appName.text.toString(),
//                AppModel.AppType.valueOf(binding.appType.selectedItem.toString()),
//                (binding.appPrice.text.toString().toFloat()*100).toInt()
//            )
            presenter.selectImage()
        }

    }

    fun showApp(app: AppModel) {
        binding.appName.setText(app.name)
        binding.appType.setSelection(app.appType.value)
        val decimalPrice = app.price/100F
        binding.appPrice.setText(decimalPrice.toString())
        Picasso.get()
            .load(app.icon)
            .into(binding.appIcon)
    }

    fun updateIcon(icon: Uri){
        i("Icon updated")
        Picasso.get()
            .load(icon)
            .into(binding.appIcon)
    }
}
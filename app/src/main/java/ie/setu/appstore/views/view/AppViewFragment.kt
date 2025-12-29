package ie.setu.appstore.views.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.appstore.R
import ie.setu.appstore.adapter.RatingAdapter
import ie.setu.appstore.databinding.FragmentAppViewBinding
import ie.setu.appstore.models.AppModel

class AppViewFragment: Fragment(R.layout.fragment_app_view) {
    private lateinit var binding: FragmentAppViewBinding
    private lateinit var presenter: AppViewPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAppViewBinding.bind(view)
        presenter = AppViewPresenter(this)

        binding.rating.setMaxValue(5)
        binding.rating.setMinValue(1)

        val layoutManager = LinearLayoutManager(this.requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = RatingAdapter(presenter.app.ratings)
        binding.recyclerView.adapter = adapter

        binding.btnEdit.setOnClickListener {
            presenter.editApp()
        }

        binding.btnRating.setOnClickListener {
            presenter.addRating(binding.rating.value)
        }
    }

    fun updateView(app: AppModel) {
        binding.appName.setText(app.name)
        binding.appType.setText(app.appType.toString())
        binding.appPrice.setText(app.priceToString())
        binding.avgRating.text = app.avgRating().toString()

        binding.recyclerView.adapter?.notifyItemRangeChanged(0, app.ratings.size)
    }
}
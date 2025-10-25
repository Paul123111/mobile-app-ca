package ie.setu.appstore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.appstore.databinding.CardPlacemarkBinding
import ie.setu.appstore.models.AppMemStore
import ie.setu.appstore.models.AppModel

class AppstoreAdapter constructor(private var apps: AppMemStore) :
    RecyclerView.Adapter<AppstoreAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlacemarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = apps.findAll()[holder.adapterPosition]
        holder.bind(placemark)
    }

    override fun getItemCount(): Int = apps.findAll().size

    class MainHolder(private val binding : CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppModel) {
            binding.placemarkTitle.text = app.name
        }
    }
}
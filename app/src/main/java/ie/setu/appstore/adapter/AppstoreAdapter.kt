package ie.setu.appstore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.appstore.databinding.CardPlacemarkBinding
import ie.setu.appstore.models.AppModel

interface AppListener {
    fun onAppClick(app: AppModel)
}

class AppstoreAdapter constructor(private var apps: List<AppModel>,
                                  private val listener: AppListener) :
    RecyclerView.Adapter<AppstoreAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlacemarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val app = apps[holder.adapterPosition]
        holder.bind(app, listener)
    }

    override fun getItemCount(): Int = apps.size

    class MainHolder(private val binding : CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppModel, listener: AppListener) {
            binding.placemarkTitle.text = app.name
            binding.root.setOnClickListener { listener.onAppClick(app) }
        }
    }
}
package ie.setu.appstore.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.appstore.R
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
        val app = apps[holder.bindingAdapterPosition]
        holder.bind(app, listener)
    }

    override fun getItemCount(): Int = apps.size

    class MainHolder(private val binding : CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppModel, listener: AppListener) {
            binding.appName.text = app.name
            binding.appPrice.text = app.priceToString()
            Picasso.get()
                .load(app.icon)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .into(binding.appIcon)
            binding.root.setOnClickListener { listener.onAppClick(app) }
        }
    }
}
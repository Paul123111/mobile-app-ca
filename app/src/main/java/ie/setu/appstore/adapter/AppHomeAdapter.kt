package ie.setu.appstore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.appstore.R
import ie.setu.appstore.databinding.CardAppHomeBinding
import ie.setu.appstore.models.AppModel

class AppHomeAdapter constructor(private var apps: List<AppModel>,
                                 private val listener: AppListener) :
    RecyclerView.Adapter<AppHomeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardAppHomeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val app = apps[holder.bindingAdapterPosition]
        holder.bind(app, listener)
    }

    override fun getItemCount(): Int = apps.size

    class MainHolder(private val binding : CardAppHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppModel, listener: AppListener) {
            binding.appName.text = app.name
            Picasso.get()
                .load(app.icon)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .into(binding.appIcon)
            binding.root.setOnClickListener { listener.onAppClick(app) }
        }
    }
}

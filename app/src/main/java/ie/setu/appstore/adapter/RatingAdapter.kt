package ie.setu.appstore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.appstore.databinding.CardPlacemarkBinding
import ie.setu.appstore.databinding.RatingCardBinding
import ie.setu.appstore.models.AppModel
import ie.setu.appstore.models.CommentModel

class RatingAdapter constructor(private var ratings: List<CommentModel>): RecyclerView.Adapter<RatingAdapter.MainHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = RatingCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val rating = ratings[holder.bindingAdapterPosition]
        holder.bind(rating)
    }

    override fun getItemCount(): Int = ratings.size

    class MainHolder(private val binding : RatingCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentModel) {
            binding.appRating.text = comment.rating.toString()
            binding.name.text = comment.username
            binding.comment.text = comment.comment
        }
    }
}
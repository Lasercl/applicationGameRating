package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.databinding.ItemRowHeroBinding

class ListGamesAdapter : ListAdapter<Game, ListGamesAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickListener: ((Game) -> Unit)? = null
    fun setOnItemClickListener(listener: (Game) -> Unit) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val gameItem = getItem(position)
        holder.bind(gameItem)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(gameItem)
        }
    }
    class MyViewHolder(val binding: ItemRowHeroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game){
//            binding.tvItem.text = "${review.review}\n- ${review.name}"
            binding.tvItemName.text="${game.name}"
            binding.tvRating.text="${game.rating}"
            Glide.with(binding.root.context)
                .load("${game.backgroundImage}")
                .into(binding.imgItemPhoto)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }
        }
    }
}
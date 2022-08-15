package com.example.testapp.ui.cakes.cakesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.testapp.R
import com.example.testapp.data.network.response.CakeResponse.CakeResponseItem
import com.example.testapp.databinding.ItemCakeBinding

class CakesAdapter(private val cakeClickCallback: ((CakeResponseItem) -> Unit)) :
    RecyclerView.Adapter<CakesAdapter.CakeViewHolder>() {

    private val cakesList: ArrayList<CakeResponseItem> = ArrayList()

    fun setCakes(list: List<CakeResponseItem>) {
        val cakeDiffUtilCallback = CakeDiffUtilCallback(cakesList, list)
        val result = DiffUtil.calculateDiff(cakeDiffUtilCallback)

        cakesList.clear()
        cakesList.addAll(list)

        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding =
            ItemCakeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val cake = cakesList[position]
        holder.bind(cake)
    }

    override fun getItemCount(): Int = cakesList.size

    inner class CakeViewHolder(private val binding: ItemCakeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                cakesList.getOrNull(adapterPosition)?.let(cakeClickCallback)
            }
        }

        fun bind(cakeItem: CakeResponseItem) {
            binding.run {
                Glide.with(cakeImage)
                    .load(cakeItem.image)
                    .error(R.drawable.unavailable_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cakeImage)

                cakeTitle.text = cakeItem.title
                cakeDesc.text = cakeItem.desc
            }
        }

    }

    private class CakeDiffUtilCallback(
        private val oldList: List<CakeResponseItem>,
        private val newList: List<CakeResponseItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true

    }

}
package org.miham.hayseed.onBoard.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miham.hayseed.databinding.RowItemsOnboardBinding

class OnBoardAdapter(private val data:List<OnBoard>) : RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {

    class ViewHolder(private val binding: RowItemsOnboardBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(ob: OnBoard) =
            with(binding) {
                imageView.setImageResource(ob.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowItemsOnboardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
package com.sarfa.mathengineservice.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sarfa.mathengineservice.R
import com.sarfa.mathengineservice.data.EquationRequest
import com.sarfa.mathengineservice.databinding.EqutationItemBinding

class EquationRequestAdapter :
    RecyclerView.Adapter<EquationRequestAdapter.EquationRequestViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Pair<EquationRequest, Double?>>() {
        override fun areItemsTheSame(
            oldItem: Pair<EquationRequest, Double?>,
            newItem: Pair<EquationRequest, Double?>
        ): Boolean {
            return oldItem.first.id == newItem.first.id
        }

        override fun areContentsTheSame(
            oldItem: Pair<EquationRequest, Double?>,
            newItem: Pair<EquationRequest, Double?>
        ): Boolean {
            return oldItem.first == newItem.first && oldItem.second == newItem.second
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquationRequestViewHolder {
        return EquationRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.equtation_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EquationRequestViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class EquationRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = EqutationItemBinding.bind(view)
        fun bind(pair: Pair<EquationRequest, Double?>) {
            viewBinding.equationText.text =
                pair.first.numbers.joinToString(" ${pair.first.operationType.label} ") + if (pair.second != null) {
                    " = ${pair.second}"
                } else ""
        }
    }
}
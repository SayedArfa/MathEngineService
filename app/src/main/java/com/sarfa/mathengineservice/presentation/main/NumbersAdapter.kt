package com.sarfa.mathengineservice.presentation.main

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarfa.mathengineservice.R
import com.sarfa.mathengineservice.databinding.AddNumberItemBinding
import com.sarfa.mathengineservice.databinding.NumberItemBinding
import com.sarfa.mathengineservice.presentation.NumberItem

class NumbersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val NUMBER_TYPE = 1
        private const val ADD_NUMBER_TYPE = 2
    }

    private var numberList = mutableListOf<NumberItem>()
    fun setItems(list: MutableList<NumberItem>) {
        numberList = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < numberList.size) NUMBER_TYPE else ADD_NUMBER_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NUMBER_TYPE) {
            NumberViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.number_item, parent, false)
            )
        } else {
            AddNumberViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.add_number_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NumberViewHolder -> {
                holder.bind(position)
            }
            is AddNumberViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return numberList.size + 1
    }

    inner class NumberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = NumberItemBinding.bind(itemView)
        private val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                numberList[adapterPosition].number = s.toString()
                numberList[adapterPosition].validationError = null
                viewBinding.numberInputText.error = null
            }

        }

        fun bind(position: Int) {
            viewBinding.removeButton.visibility =
                if (numberList.size > 2) View.VISIBLE else View.INVISIBLE
            viewBinding.removeButton.setOnClickListener {
                numberList.removeAt(position)
                notifyDataSetChanged()
            }

            viewBinding.numberInputText.editText?.removeTextChangedListener(textWatcher)
            viewBinding.numberInputText.editText?.setText(numberList[position].number)
            viewBinding.numberInputText.error = numberList[position].validationError
            viewBinding.numberInputText.editText?.addTextChangedListener(textWatcher)

        }
    }

    inner class AddNumberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = AddNumberItemBinding.bind(itemView)
        fun bind() {
            viewBinding.addButton.setOnClickListener {
                numberList.add(NumberItem(""))
                notifyDataSetChanged()
            }
        }
    }
}
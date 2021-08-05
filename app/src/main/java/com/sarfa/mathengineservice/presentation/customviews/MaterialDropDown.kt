package com.sarfa.mathengineservice.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout


class MaterialDropDown : TextInputLayout {
    companion object {
        data class DropDownItem(val value: String, val label: String)
    }

    var onItemClickListener: AdapterView.OnItemClickListener? = null

    private var items = mutableListOf<DropDownItem>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setItems(values: List<DropDownItem>) {
        items = mutableListOf<DropDownItem>().apply {
            addAll(values)
        }
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                items.map {
                    it.label
                })
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        (editText as? AutoCompleteTextView)?.setAdapter(
            dataAdapter
        )
        (editText as? AutoCompleteTextView)?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                setSelection(position)
                onItemClickListener?.onItemClick(parent, view, position, id)
            }
    }

    fun setError(error: String?) {
        error?.let {
            this.error = error
        } ?: run {
            isErrorEnabled = false
            this.error = null
            resetStatusText()
        }
    }

    private fun resetStatusText() {
        isHelperTextEnabled = false
        helperText = ""
        startIconDrawable = null
    }

    fun setSelection(position: Int) {
        (editText as? AutoCompleteTextView)?.setText(items[position].label, false)
        (editText as? AutoCompleteTextView)?.tag = items[position].value
    }

    fun setSelectionEmpty() {
        (editText as? AutoCompleteTextView)?.setText("", false)
        (editText as? AutoCompleteTextView)?.tag = ""
    }

    fun getSelectedValue(): String {
        return ((editText as? AutoCompleteTextView)?.tag as? String) ?: ""
    }
}
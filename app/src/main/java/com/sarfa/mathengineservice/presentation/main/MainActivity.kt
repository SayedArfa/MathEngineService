package com.sarfa.mathengineservice.presentation.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarfa.mathengineservice.core.extensions.hideKeyboard
import com.sarfa.mathengineservice.core.util.EventObserver
import com.sarfa.mathengineservice.core.viewmodel.ViewModelFactory
import com.sarfa.mathengineservice.databinding.ActivityMainBinding
import com.sarfa.mathengineservice.presentation.customviews.MaterialDropDown
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<MainViewModel>

    private lateinit var viewBinding: ActivityMainBinding

    lateinit var mainViewModel: MainViewModel
    private val timeTextChangeListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            mainViewModel.selectedTime = s.toString()
            viewBinding.timeInputText.isErrorEnabled = false
            viewBinding.timeInputText.error = null
        }

    }
    private lateinit var numbersLayoutManager: GridLayoutManager
    private val numbersAdapter = NumbersAdapter()

    private lateinit var pendingRequestLayoutManager: LinearLayoutManager
    private val pendingRequestAdapter = EquationRequestAdapter()

    private lateinit var completedRequestLayoutManager: LinearLayoutManager
    private val completedRequestAdapter = EquationRequestAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        initOperationTypeDropDown()
        initTime()
        initNumbers()
        initEquationsRecyclerView()

        viewBinding.calculateButton.setOnClickListener {
            it?.hideKeyboard()
            calculate()
        }

        mainViewModel.validationErrorLiveData.observe(this, EventObserver {
            viewBinding.timeInputText.isErrorEnabled = it.delayTimeError != null
            viewBinding.timeInputText.error = it.delayTimeError
            numbersAdapter.setItems(mainViewModel.selectedNumbers)
        })

        mainViewModel.pendingRequestsLiveData.observe(this) {
            pendingRequestAdapter.differ.submitList(it)

        }
        mainViewModel.completedRequestsLiveData.observe(this) {
            completedRequestAdapter.differ.submitList(it)
        }
    }

    private fun initOperationTypeDropDown() {
        val items = mutableListOf<MaterialDropDown.Companion.DropDownItem>().apply {
            addAll(mainViewModel.allOperationTypes.map {
                MaterialDropDown.Companion.DropDownItem(it.name, it.label)
            })
        }
        viewBinding.operationTypeDropDown.setItems(items)
        viewBinding.operationTypeDropDown.setSelection(mainViewModel.selectedOperationPos)
        viewBinding.operationTypeDropDown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                mainViewModel.selectedOperationPos = position
            }
    }

    private fun initTime() {
        viewBinding.timeInputText.editText?.removeTextChangedListener(timeTextChangeListener)
        viewBinding.timeInputText.editText?.setText(mainViewModel.selectedTime)
        viewBinding.timeInputText.editText?.addTextChangedListener(timeTextChangeListener)
    }

    private fun initNumbers() {
        numbersLayoutManager = GridLayoutManager(this, 3)
        viewBinding.numbersRecyclerView.layoutManager = numbersLayoutManager
        viewBinding.numbersRecyclerView.adapter = numbersAdapter
        numbersAdapter.setItems(mainViewModel.selectedNumbers)
    }

    private fun initEquationsRecyclerView() {
        pendingRequestLayoutManager = LinearLayoutManager(this)
        viewBinding.pendingRequest.layoutManager = pendingRequestLayoutManager
        viewBinding.pendingRequest.adapter = pendingRequestAdapter

        completedRequestLayoutManager = LinearLayoutManager(this)
        viewBinding.completedRequest.layoutManager = completedRequestLayoutManager
        viewBinding.completedRequest.adapter = completedRequestAdapter
    }

    private fun calculate() {
        mainViewModel.calculateEquation()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.startBindService()
    }

    override fun onStop() {
        mainViewModel.stopBindService()
        super.onStop()
    }
}
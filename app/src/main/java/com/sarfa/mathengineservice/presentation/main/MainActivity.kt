package com.sarfa.mathengineservice.presentation.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sarfa.mathengineservice.data.EquationRequest
import com.sarfa.mathengineservice.databinding.ActivityMainBinding
import com.sarfa.mathengineservice.presentation.customviews.MaterialDropDown
import com.sarfa.mathengineservice.services.MathEngineService
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private var mathEngineService: MathEngineService? = null
    private var isBound = false
    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    lateinit var mainViewModel: MainViewModel
    private val timeTextChangeListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            mainViewModel.selectedTime = s.toString()
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
        initOperationTypeDropDown()
        initTime()
        initNumbers()
        initEquationsRecyclerView()

        viewBinding.calculateButton.setOnClickListener {
            Intent(this, MathEngineService::class.java).also { intent ->
                intent.putExtra(
                    MathEngineService.EQUATION_REQUEST_KEY,
                    EquationRequest(
                        mainViewModel.selectedNumbers.map {
                            it.toDouble()
                        },
                        mainViewModel.allOperationTypes[mainViewModel.selectedOperationPos],
                        mainViewModel.selectedTime.toLong()
                    )
                )
                startService(intent)
            }
        }
//        sendTestRequests()
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

    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            isBound = true
            (binder as? MathEngineService.LocalBinder)?.let {
                mathEngineService = it.service
                it.service.requestsLiveData.observe(this@MainActivity) {

                    val pendingRequests = it.filter {
                        it.second == null
                    }
                    val completedRequests = it.filter {
                        it.second != null
                    }

                    pendingRequestAdapter.differ.submitList(pendingRequests)
                    completedRequestAdapter.differ.submitList(completedRequests)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            mathEngineService = null
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MathEngineService::class.java)
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (isBound)
            unbindService(boundServiceConnection)
        super.onStop()
    }
}
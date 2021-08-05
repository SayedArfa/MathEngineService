package com.sarfa.mathengineservice.presentation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sarfa.mathengineservice.R
import com.sarfa.mathengineservice.data.EquationRequest
import com.sarfa.mathengineservice.data.OperationType
import com.sarfa.mathengineservice.services.MathEngineService

class MainActivity : AppCompatActivity() {
    private var mathEngineService: MathEngineService? = null
    private var isBound = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, MathEngineService::class.java).also { intent ->
            intent.putExtra(
                MathEngineService.EQUATION_REQUEST_KEY,
                EquationRequest(listOf(1.0, 2.0,-2.0), OperationType.ADD, 30)
            )
            startService(intent)
        }
        Intent(this, MathEngineService::class.java).also { intent ->
            intent.putExtra(
                MathEngineService.EQUATION_REQUEST_KEY,
                EquationRequest(listOf(1.0, 2.0,3.0), OperationType.SUB, 20)
            )
            startService(intent)
        }
        Intent(this, MathEngineService::class.java).also { intent ->
            intent.putExtra(
                MathEngineService.EQUATION_REQUEST_KEY,
                EquationRequest(listOf(1.0, 2.0,0.0), OperationType.MUL, 10)
            )
            startService(intent)
        }
        Intent(this, MathEngineService::class.java).also { intent ->
            intent.putExtra(
                MathEngineService.EQUATION_REQUEST_KEY,
                EquationRequest(listOf(1.0, 2.0,0.0), OperationType.DIV, 5)
            )
            startService(intent)
        }
    }

    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            isBound = true
            (binder as? MathEngineService.LocalBinder)?.let {
                mathEngineService = it.service
                it.service.requestsLiveData.observe(this@MainActivity) {
                    Log.d("sayed", it.toString())
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
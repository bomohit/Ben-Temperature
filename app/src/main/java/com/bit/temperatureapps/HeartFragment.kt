package com.bit.temperatureapps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bit.temperatureapps.data.User
import com.bit.temperatureapps.data.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import net.kibotu.heartrateometer.HeartRateOmeter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HeartFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    var subscription: CompositeDisposable? = null
    var req = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val heartButton: ImageView = view.findViewById(R.id.heartImg)

        heartButton.setOnClickListener {
            heartButton.setImageResource(R.drawable.temp_red_bg)
            if (!req) {
                var fingerRead: String? = null
                req = true
                var fingerOn = false
                val bpmUpdates = HeartRateOmeter()
                    .withAverageAfterSeconds(3)
                    .setFingerDetectionListener(this::onFingerChange)
                    .bpmUpdates(view.findViewById(R.id.preview))
                    .subscribe({
                            bpm: HeartRateOmeter.Bpm? ->
                        d("bomoh", "bpm1: $bpm bpm")

                        // Wait for finger
                        if (bpm!!.value > 0) {
                            d("bomoh", "bpm1: true")
                            view.findViewById<TextView>(R.id.bpmRate).text = bpm!!.value.toString()
                            fingerRead = bpm!!.value.toString()
                            fingerOn = true
                        } else {
                            fingerOn = false
                        }

                    }, Throwable::printStackTrace)
                subscription?.add(bpmUpdates)

                Handler(Looper.getMainLooper()).postDelayed( {
                        dispose()
                        subscription = CompositeDisposable()
                        d("bomoh", "Button:DISPOSE")
                        req = false
                    if (!fingerOn) {
                        d("bomoh", "fingerOff")
                        heartButton.setBackgroundResource(R.drawable.heart_bg)
                        view.findViewById<TextView>(R.id.heartInfoGuide).text = "Failed , Try again"
                    } else {
                        d("bomoh", "fingerON")
                        // Success getting reading
                        // Send read data to datbase ROOM
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val formatted = current.format(formatter)
                        val temperature = arguments?.getString("temperature").toString()
                        val user = User(0, formatted, temperature, fingerRead.toString())
//                        mUserViewModel.addUser(user)
                    }

                },10000 )
            }

        }
    }

    override fun onResume() {
        super.onResume()

        dispose()
        subscription = CompositeDisposable()

        startWithPermissionCheck()
        d("bomoh", "onResume")
    }

    override fun onPause() {
        super.onPause()
        dispose()
        d("bomoh", "onDispose")
    }

    private fun dispose() {
        d("bomoh", "fun:dispose")
        d("bomoh", "sub:${subscription?.isDisposed}")
        if (subscription?.isDisposed == false) {
            subscription?.dispose()
            d("bomoh", "fun:dispose:false")
        }

        else if (subscription?.isDisposed == true) {
            d("bomoh", "fun:dispose:true")
        }

    }

    @SuppressLint("CheckResult")
    private fun startWithPermissionCheck() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            checkPermissions(REQUEST_CAMERA_PERMISSION, Manifest.permission.CAMERA)
            return
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onBpm(bpm: HeartRateOmeter.Bpm) {
        // Log.v("HeartRateOmeter", "[onBpm] $bpm")
//        label.text = "$bpm bpm"
        d("bomoh", "bpm: $bpm bpm")
    }

    @SuppressLint("SetTextI18n")
    private fun onFingerChange(fingerDetected: Boolean){
//        finger.text = "$fingerDetected"
        d("bomoh", "finger: $fingerDetected")
        if (fingerDetected) {
            requireView().findViewById<TextView>(R.id.heartInfoGuide).text = "Dont move your finger for 10 seconds"
        } else {
            requireView().findViewById<TextView>(R.id.heartInfoGuide).text = "Put your finger on the camera and flash"
        }
    }

    private fun hasPermission(vararg permissionsId: String): Boolean {
        var hasPermission = true

        permissionsId.forEach { permission ->
            hasPermission = hasPermission
                    && ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }

        return hasPermission
    }

    private fun checkPermissions(callbackId: Int, vararg permissionsId: String) {
        when {
            !hasPermission(*permissionsId) -> try {
                ActivityCompat.requestPermissions(requireActivity(), permissionsId, callbackId)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWithPermissionCheck()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 123
    }
}
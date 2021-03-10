package com.bit.temperatureapps

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context.USB_SERVICE
import android.content.Intent
import android.hardware.usb.UsbManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ScanFragment : Fragment(), SerialInputOutputManager.Listener {
    private var baudRate = 9600
    var temperatureTaken : String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_scan, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            findNavController().navigate(R.id.action_ScanFragment_to_HomeFragment)
//        }

        view.findViewById<Button>(R.id.buttonNext).setOnClickListener {
//            val bundle = bundleOf("temperature" to "36.7")
            val bundle = bundleOf("temperature" to temperatureTaken.toString())
            findNavController().navigate(R.id.action_ScanFragment_to_HeartFragment, bundle)
        }

        view.findViewById<ImageView>(R.id.imageView4).setOnClickListener {
            getDataFromArduino()
        }



    }

    fun getDataFromArduino() {
        // Find all available drivers from attached devices.
        // Find all available drivers from attached devices.
        val manager = requireContext().getSystemService(USB_SERVICE) as UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
        if (availableDrivers.isEmpty()) {

            return
        }

//        Snackbar.make(requireView(), availableDrivers.toString(), Snackbar.LENGTH_SHORT).show()

        // Open a connection to the first available driver.

        // Open a connection to the first available driver.
        val driver = availableDrivers[0]
        val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION" //#
        val usbPermissionIntent = PendingIntent.getBroadcast( //#
                requireContext(), 0, Intent(ACTION_USB_PERMISSION), 0
        )
        val connection = manager.openDevice(driver.device)
        manager.requestPermission(driver.device, usbPermissionIntent) //#
                ?: // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
                return

        //# CHECK IF GOT PERMISSION OR NOT
        if (connection == null) { //#
            if (manager.hasPermission(driver.device)) {
//                findViewById<TextView>(R.id.textView2).text = "GOT permission" //#
            } else {
//                Toast.makeText(requireContext(), "NO PERMISSION", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            if (manager.hasPermission(driver.device)) {
//                Toast.makeText(requireContext(), "GOT PERMISSION", Toast.LENGTH_SHORT).show()


            } else {
//                findViewById<TextView>(R.id.textView2).text = "2NO permission" //#
                return
            }
        }

        val port = driver.ports[0] // Most devices have just one port (port 0)

        port.open(connection)
        port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        port.dtr = true
        port.rts = true
//                val buffer = ByteArray(8192)
//                val len  = port.read(buffer, 2000)
//                findViewById<TextView>(R.id.textRead).text = String(buffer)
//                requireActivity().findViewById<TextView>(R.id.displayTemperature).text = "${String(buffer)} °C"
//                port.close()

        try {
            val usbIoManager = SerialInputOutputManager(port, this)
            Executors.newSingleThreadExecutor().submit(usbIoManager)
        } catch (e: IllegalArgumentException) {
            port.close()
            Toast.makeText(requireContext(), "Problem: $e", Toast.LENGTH_LONG).show()
            Executors.newSingleThreadExecutor().shutdown()
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onNewData(data: ByteArray?) {
//        Toast.makeText(v!!, "${String(data!!)}", Toast.LENGTH_SHORT).show()
//        this.requireActivity().findViewById<TextView>(R.id.displayTemperature).text = "${String(data!!)} °C"
        val mp = MediaPlayer.create(requireContext(), R.raw.scan);
        d("bomoh", "yes ${String(data!!)}")
        if (!String(data).contains("error")) {
            temperatureTaken = String(data)
            mp.start()
            requireActivity().runOnUiThread {
                requireActivity().findViewById<TextView>(R.id.displayTemperature).text = "${String(data!!)} °C"

            }
            requireActivity().runOnUiThread {
                requireActivity().findViewById<Button>(R.id.buttonNext).isEnabled = true
            }
        }
    }

    override fun onRunError(e: Exception?) {
        Toast.makeText(requireContext(), "Problem: $e", Toast.LENGTH_LONG).show()
    }


}
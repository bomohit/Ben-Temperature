package com.bit.temperatureapps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ConstraintLayout>(R.id.scanButton).setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_ScanFragment)
        }

        view.findViewById<ConstraintLayout>(R.id.settingButton).setOnClickListener {

        }

        view.findViewById<ConstraintLayout>(R.id.historyButton).setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_HistoryFragment)
        }

    }
}
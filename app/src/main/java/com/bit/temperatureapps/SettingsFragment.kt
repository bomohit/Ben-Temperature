package com.bit.temperatureapps

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.bit.temperatureapps.data.UserViewModel
import com.google.android.material.snackbar.Snackbar


class SettingsFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonClear).setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_,_ ->
                mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
                mUserViewModel.deleteAllUsers()
                Snackbar.make(it, "History log cleared", Snackbar.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") {_, _ -> }
            builder.setTitle("Clear History log?")
            builder.setMessage("Are you sure you want to delete ?")
            builder.create().show()
        }
    }
}
package com.example.insutagram.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insutagram.*
import com.example.insutagram.databinding.ActivityMainBinding
import com.example.insutagram.databinding.HomeFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.home_fragment){

    private var _binding: HomeFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel: MyViewModel by viewModels()
        binding.postRecyclerView.adapter = CustomAdapter(viewModel)
        binding.postRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.postRecyclerView.setHasFixedSize(true)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

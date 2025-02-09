package com.example.thriftclothing.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thriftclothing.databinding.FragmentHomeBinding
import com.example.thriftclothing.ui.activity.AvailableJacketActivity
import com.example.thriftclothing.ui.activity.AvailableJeansActivity
import com.example.thriftclothing.ui.activity.AvailableShoesActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnShoes.setOnClickListener {
            startActivity(Intent(requireContext(), AvailableShoesActivity::class.java))
        }

        binding.btnJeans.setOnClickListener {
            startActivity(Intent(requireContext(), AvailableJeansActivity::class.java))
        }

        binding.btnJacket.setOnClickListener {
            startActivity(Intent(requireContext(), AvailableJacketActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

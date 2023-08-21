package com.example.fishknowconnect.ui.fish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.databinding.FragmentWaterBinding

class WaterFragment : Fragment() {

    private var _binding: FragmentWaterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val waterViewModel = ViewModelProvider(this).get(WaterViewModel::class.java)
        _binding = FragmentWaterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textViewWater
        waterViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
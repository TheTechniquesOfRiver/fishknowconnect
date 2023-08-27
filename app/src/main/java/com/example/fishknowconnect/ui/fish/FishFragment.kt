package com.example.fishknowconnect.ui.fish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.databinding.FragmentFishBinding

class FishFragment : Fragment() {

    private var _binding: FragmentFishBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val fishViewModel = ViewModelProvider(this).get(FishViewModel::class.java)
        _binding = FragmentFishBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textViewFish
        fishViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
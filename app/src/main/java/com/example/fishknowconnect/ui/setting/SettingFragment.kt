package com.example.fishknowconnect.ui.setting

import LocaleHelper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fishknowconnect.ui.NavigationDrawerActivity
import com.example.fishknowconnect.R
import com.example.fishknowconnect.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textSlideshow
        settingViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        //      Initialize the button by finding it by its ID
        val buttonBangala = root.findViewById<RadioButton>(R.id.radio_bangla)
        val buttonEnglish = root.findViewById<RadioButton>(R.id.radio_eng)
        //change into bangla language
        buttonBangala.setOnClickListener {
            LocaleHelper.setLocale(this.requireContext(), "bn");
            val i = Intent(this.requireContext(), NavigationDrawerActivity::class.java)
            startActivity(i)
        }
        // change into english language
        buttonEnglish.setOnClickListener {
            LocaleHelper.setLocale(this.requireContext(), "en");
            val i = Intent(this.requireContext(), NavigationDrawerActivity::class.java)
            startActivity(i)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
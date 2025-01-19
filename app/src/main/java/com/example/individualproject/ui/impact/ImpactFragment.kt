package com.example.individualproject.ui.impact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.individualproject.AddPostActivity
import com.example.individualproject.TreeActivity
import com.example.individualproject.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val impactViewModel =
            ViewModelProvider(this).get(ImpactViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.treesPlanted
        impactViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val plantTrees = binding.impactPlantTreesButton

        plantTrees.setOnClickListener {
            val intent = Intent(context, TreeActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
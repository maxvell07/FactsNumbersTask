package com.example.factsnumberstask.details.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.factsnumberstask.R
import com.example.factsnumberstask.numbers.presentation.NumbersFragment

class DetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmet_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val value: String = requireArguments().getString(KEY).toString()

        view.findViewById<TextView>(R.id.textView).text = value
    }


    companion object {
        const val KEY = "details"

        fun newInstance(value: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(KEY, value)
            }
        }
    }

}
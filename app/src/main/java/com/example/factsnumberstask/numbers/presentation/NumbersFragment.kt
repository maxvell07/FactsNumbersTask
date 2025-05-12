package com.example.factsnumberstask.numbers.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.factsnumberstask.R
import com.example.factsnumberstask.details.presentation.DetailsFragment
import com.example.factsnumberstask.main.presentation.ShowFragment
import com.example.factsnumberstask.main.sl.ProvideViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NumbersFragment : Fragment() {

    private var showFragment: ShowFragment = ShowFragment.Empty()
    private lateinit var viewModel: NumbersViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showFragment = context as ShowFragment
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragmet_numbers, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity() as ProvideViewModel).provideViewModel(
            NumbersViewModel::class.java, this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressbar)
        val factButton = view.findViewById<Button>(R.id.getFact)
        val randomButton = view.findViewById<Button>(R.id.getRandomFact)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val inputEditText = view.findViewById<TextInputEditText>(R.id.edittext)
        val inputLayout = view.findViewById<TextInputLayout>(R.id.textInputLayout)
        val adapter = NumbersAdapter(object : ClickListener {
            override fun click(item: NumberUi) {
                //        val detailsFragment = DetailsFragment.newInstance("some information for number")
                //      showFragment.show(detailsFragment)
                //todo move to next screen(fragment)

            }
        })
        recyclerView.adapter = adapter
        inputEditText.addTextChangedListener(object : simpleTextWatcher(){
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                viewModel.clearError()
            }
        })
        factButton.setOnClickListener {
            viewModel.fetchNumberFact(inputEditText.text.toString())
        }

        randomButton.setOnClickListener {
            viewModel.fetchRandomNumberFact()
        }

        viewModel.observeState(this) {
            it.apply(inputLayout,inputEditText)
        }

        viewModel.observeList(this) {
            adapter.map(it)
        }

        viewModel.observeProgress(this) {
            progressBar.visibility = it
        }
        
        viewModel.init(savedInstanceState == null)

    }

    override fun onDetach() {
        super.onDetach()
        showFragment = ShowFragment.Empty()
    }

}

abstract class simpleTextWatcher: TextWatcher{
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable?) = Unit
}
package com.example.factsnumberstask.main.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.example.factsnumberstask.R
import com.example.factsnumberstask.main.sl.ProvideViewModel
import com.example.factsnumberstask.numbers.presentation.NumbersFragment

class MainActivity : AppCompatActivity(), ShowFragment, ProvideViewModel {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            show(NumbersFragment(), false)
    }

    override fun show(fragment: Fragment) {
        show(fragment, true)
    }


    private fun show(fragment: Fragment, add: Boolean) {
        //todo
        val transaction = supportFragmentManager.beginTransaction()
        val conteiner = R.id.container
        if (add) {
            transaction.add(conteiner, fragment).addToBackStack(fragment.javaClass.simpleName)
        } else {
            transaction.replace(conteiner, fragment)
        }
        transaction.commit()
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T =
        (application as ProvideViewModel).provideViewModel(clazz, owner)

}

interface ShowFragment {

    fun show(fragment: Fragment)
    class Empty() : ShowFragment {
        override fun show(fragment: Fragment) = Unit

    }
}

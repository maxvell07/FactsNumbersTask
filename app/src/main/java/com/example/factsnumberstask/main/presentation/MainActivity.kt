package com.example.factsnumberstask.main.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.factsnumberstask.R
import com.example.factsnumberstask.numbers.presentation.NumbersFragment

class MainActivity : AppCompatActivity(), ShowFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            show(NumbersFragment(),false)
    }

    override fun show(fragment: Fragment) {
        show(fragment, true)
    }


    private fun show(fragment: Fragment, add: Boolean) {
        //todo
        val transaction = supportFragmentManager.beginTransaction()
        val conteiner = R.id.container
        if (add) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        } else {
            transaction.replace(conteiner, fragment)
        }
        transaction.commit()
    }

}

interface ShowFragment {

    fun show(fragment: Fragment)
    class Empty() : ShowFragment {
        override fun show(fragment: Fragment) = Unit

    }
}

package ru.skillbranch.gameofthrones.ui.houses


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.skillbranch.gameofthrones.R

/**
 * A simple [Fragment] subclass.
 */
class HousesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_houses, container, false)
    }


}
package ru.skillbranch.gameofthrones.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_characters_list_screen.*
import kotlinx.android.synthetic.main.fragment_characters_list_screen.view.*
import ru.skillbranch.gameofthrones.R

/**
 * A placeholder fragment containing a simple view.
 */
class CharactersListFragment : Fragment() {

    private lateinit var viewModel: CharactersListViewModel

    private val charactersAdapter = CharactersAdapter {
        Snackbar.make(this.recycledView, it.name, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CharactersListViewModel::class.java).apply {
            setHouseName(arguments?.getString(ARG_HOUSE_NAME) ?: "Unknown")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_characters_list_screen, container, false)

        val divider = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        with(root.recycledView) {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(divider)
        }

        viewModel.characters.observe(this, Observer {
            charactersAdapter.items = it
        })
        return root
    }

    companion object {
        private const val ARG_HOUSE_NAME = "ARG_HOUSE_NAME"

        @JvmStatic
        fun newInstance(houseName: String): CharactersListFragment {
            return CharactersListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HOUSE_NAME, houseName)
                }
            }
        }
    }
}
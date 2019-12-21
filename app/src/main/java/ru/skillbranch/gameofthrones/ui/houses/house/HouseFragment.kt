package ru.skillbranch.gameofthrones.ui.houses.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_house_list.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.ui.custom.ItemDivider

class HouseFragment : Fragment() {


    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var viewModel: HouseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val house = HouseType.valueOf(arguments?.getString(HOUSE_NAME) ?: HouseType.STARK.name)
        val vmFactory = HouseViewModelFactory(house)
        characterAdapter = CharacterAdapter {
            //            val action TODO
        }

        viewModel = ViewModelProviders.of(this, vmFactory).get(HouseViewModel::class.java)
        viewModel.getCharacters().observe(this, Observer {
            characterAdapter.submitList(it)
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        with(menu.findItem(R.id.action_search).actionView as SearchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.handleSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.handleSearchQuery(newText)
                    return true
                }
            })
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_house_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(house_characters_list) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemDivider())
            adapter = characterAdapter
        }
    }


    companion object {
        private const val HOUSE_NAME = "HOUSE_NAME"

        @JvmStatic
        fun newInstance(houseType: HouseType) =
            HouseFragment().apply {
                arguments = bundleOf(HOUSE_NAME to houseType.name)
            }
    }
}


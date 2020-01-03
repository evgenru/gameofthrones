package ru.skillbranch.gameofthrones.ui.character


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_character.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.ui.RootActivity

class CharacterFragment : Fragment() {
    private val args: CharacterFragmentArgs by navArgs()
    private lateinit var viewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, CharacterViewModelFactory(args.id))
            .get(CharacterViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val houseType = args.house
        val arms = args.house.coastOfArms
        val scrim = args.house.primaryColor
        val scrimDark = args.house.darkColor

        val rootActivity = requireActivity() as RootActivity
        rootActivity.setSupportActionBar(toolbar)
        rootActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = args.title
        }

        iv_arms.setImageResource(arms)
        with(collapsing_layout) {
            setBackgroundResource(scrim)
            setContentScrimResource(scrim)
            setStatusBarScrimResource(scrimDark)
        }

        collapsing_layout.post { collapsing_layout.requestLayout() } // fix collapsed title bug

        viewModel.getCharacter().observe(this, Observer { character ->
            if (character == null) return@Observer

            val iconColor = requireContext().getColor(houseType.accentColor)

            listOf(tv_words_label, tv_born_label, tv_titles_label, tv_aliases_label)
                .forEach { it.compoundDrawables.firstOrNull()?.setTint(iconColor) }

            tv_words.text = character.words
            tv_born.text = character.born
            tv_titles.text = character.titles
                .filter { it.isNotEmpty() }
                .joinToString("\n")
            tv_aliases.text = character.aliases
                .filter { it.isNotEmpty() }
                .joinToString("\n")

            character.father?.let {
                group_father.isVisible = true
                btn_father.text = it.name
                val action = CharacterFragmentDirections.actionCharacterFragmentSelf(
                    it.id,
                    HouseType.fromString(it.house),
                    it.name
                )
                btn_father.setOnClickListener { findNavController().navigate(action) }
            }

            character.mother?.let {
                group_mother.isVisible = true
                btn_mother.text = it.name
                val action = CharacterFragmentDirections.actionCharacterFragmentSelf(
                    it.id,
                    HouseType.fromString(it.house),
                    it.name
                )
                btn_mother.setOnClickListener { findNavController().navigate(action) }
            }

            if (character.died.isNotBlank()) {
                Snackbar.make(
                    coordinator,
                    "Died in : ${character.died}",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}

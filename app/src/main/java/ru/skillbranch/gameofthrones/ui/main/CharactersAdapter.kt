package ru.skillbranch.gameofthrones.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.listitem_characters.view.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem

class CharactersAdapter(private val listener: (CharacterItem) -> Unit): RecyclerView.Adapter<CharactersAdapter.CharacterItemViewHolder>() {

    var items: List<CharacterItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.listitem_characters, parent, false)
        return CharacterItemViewHolder(convertView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CharacterItemViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    class CharacterItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: CharacterItem, listener: (CharacterItem) -> Unit) {
            containerView.house_icon.setImageResource(item.house.toHouseIconResource())
            containerView.character_name.text = item.name.ifEmpty { "Information is unknown" }
            containerView.character_description.text = item.titles.joinToString(" • ").ifEmpty { "Information is unknown" }
            containerView.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}

private fun String.toHouseIconResource() = when(this){
    "Stark" -> R.drawable.stark_icon
    "Lannister" -> R.drawable.lannister_icon
    "Targaryen" -> R.drawable.targaryen_icon
    "Greyjoy" -> R.drawable.greyjoy_icon
    "Tyrell" -> R.drawable.tyrel_icon
    "Baratheon" -> R.drawable.baratheon_icon
    "Nymeros Martell" -> R.drawable.martel_icon
    else -> error("Unknown House")
}

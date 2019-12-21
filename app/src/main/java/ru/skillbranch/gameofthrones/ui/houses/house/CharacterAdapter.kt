package ru.skillbranch.gameofthrones.ui.houses.house

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.listitem_characters.view.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.HouseType

class CharacterAdapter(private val listener: (CharacterItem) -> Unit) :
    ListAdapter<CharacterItem, CharacterAdapter.CharacterVH>(
        DIFF_CALLBACK
    ) {


    class CharacterVH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindTo(item: CharacterItem, listener: (CharacterItem) -> Unit) {
            containerView.house_icon.setImageResource(HouseType.fromString(item.house).icon)
            containerView.character_name.text = item.name.ifEmpty { "Information is unknown" }
            containerView.character_description.text =
                item.titles.joinToString(" • ").ifEmpty { "Information is unknown" }
            containerView.setOnClickListener {
                listener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVH {
        val containerView = LayoutInflater.from(parent.context).inflate(
            R.layout.listitem_characters,
            parent,
            false
        )
        return CharacterVH(containerView)
    }

    override fun onBindViewHolder(holder: CharacterVH, position: Int) {
        holder.bindTo(getItem(position), listener)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterItem>() {
            override fun areItemsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CharacterItem,
                newItem: CharacterItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


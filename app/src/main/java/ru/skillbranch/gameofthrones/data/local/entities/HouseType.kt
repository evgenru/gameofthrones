package ru.skillbranch.gameofthrones.data.local.entities

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import ru.skillbranch.gameofthrones.R

enum class HouseType(
    val title: String,
    @DrawableRes val icon: Int,
    @DrawableRes val coastOfArms: Int,
    @ColorRes val primaryColor: Int,
    @ColorRes val accentColor: Int,
    @ColorRes val darkColor: Int
) {
    STARK("Stark", R.drawable.stark_icon, R.drawable.stark_coast_of_arms, R.color.stark_primary, R.color.stark_accent, R.color.stark_accent),
    LANNISTER("Lannister", R.drawable.lannister_icon, R.drawable.lannister_coast_of_arms, R.color.lannister_primary, R.color.lannister_accent, R.color.lannister_accent),
    TARGARYEN("Targaryen", R.drawable.targaryen_icon, R.drawable.targaryen_coast_of_arms, R.color.targaryen_primary, R.color.targaryen_accent, R.color.targaryen_accent),
    BARATHEON("Baratheon", R.drawable.baratheon_icon, R.drawable.baratheon_coast_of_arms, R.color.baratheon_primary, R.color.baratheon_accent, R.color.baratheon_accent),
    GREYJOY("Greyjoy", R.drawable.greyjoy_icon, R.drawable.greyjoy_coast_of_arms, R.color.greyjoy_primary, R.color.greyjoy_accent, R.color.greyjoy_accent),
    MARTELL("Martell", R.drawable.martel_icon, R.drawable.martel_coast_of_arms, R.color.martel_primary, R.color.martel_accent, R.color.martel_accent),
    TYRELL("Tyrell", R.drawable.tyrel_icon, R.drawable.tyrel_coast_of_arms, R.color.tyrel_primary, R.color.tyrel_accent, R.color.tyrel_accent);

    companion object {
        fun fromString (title: String):HouseType{
            return when(title){
                "Stark" -> STARK
                "Lannister" -> LANNISTER
                "Targaryen" -> TARGARYEN
                "Baratheon" -> BARATHEON
                "Greyjoy" -> GREYJOY
                "Martell" -> MARTELL
                "Tyrell" -> TYRELL
                else -> error("unknown house $title")
            }
        }
    }
}

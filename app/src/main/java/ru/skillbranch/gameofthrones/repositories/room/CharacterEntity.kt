package ru.skillbranch.gameofthrones.repositories.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter

@Entity
data class CharacterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val father: String, //rel
    val mother: String //rel
)

@Entity
data class CharacterHouseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val words: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val father: String, //rel
    val mother: String, //rel
    val house: String
)
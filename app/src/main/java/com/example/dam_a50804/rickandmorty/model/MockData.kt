package com.example.dam_a50804.rickandmorty.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String = ""
)

data class Episode(
    val id: Int,
    val name: String,
    val episodeCode: String,
    val airDate: String
)

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

object MockData {
    val characters = listOf(
        Character(1, "Rick Sanchez", "Alive", "Human"),
        Character(2, "Morty Smith", "Alive", "Human"),
        Character(3, "Summer Smith", "Alive", "Human"),
        Character(4, "Beth Smith", "Alive", "Human"),
        Character(5, "Jerry Smith", "Alive", "Human"),
        Character(6, "Abadango Cluster Princess", "Alive", "Alien"),
        Character(7, "Abradolf Lincler", "unknown", "Human, Genetic experiment"),
        Character(8, "Adjudicator Rick", "Dead", "Human"),
        Character(9, "Agency Director", "Dead", "Human"),
        Character(10, "Alan Rails", "Dead", "Human")
    )

    val episodes = listOf(
        Episode(1, "Pilot", "S01E01", "December 2, 2013"),
        Episode(2, "Lawnmower Dog", "S01E02", "December 9, 2013"),
        Episode(3, "Anatomy Park", "S01E03", "December 16, 2013"),
        Episode(4, "M. Night Shaym-Aliens!", "S01E04", "January 13, 2014"),
        Episode(5, "Meeseeks and Destroy", "S01E05", "January 20, 2014"),
        Episode(6, "Rick Potion #9", "S01E06", "January 27, 2014"),
        Episode(7, "Raising Gazorpazorp", "S01E07", "March 10, 2014"),
        Episode(8, "Rixty Minutes", "S01E08", "March 17, 2014"),
        Episode(9, "Something Ricked This Way Comes", "S01E09", "March 24, 2014"),
        Episode(10, "Close Rick-counters of the Rick Kind", "S01E10", "April 7, 2014")
    )

    val locations = listOf(
        Location(1, "Earth (C-137)", "Planet", "Dimension C-137"),
        Location(2, "Abadango", "Cluster", "unknown"),
        Location(3, "Citadel of Ricks", "Space station", "unknown"),
        Location(4, "Worldender's lair", "Planet", "unknown"),
        Location(5, "Anatomy Park", "Microverse", "Dimension C-137"),
        Location(6, "Interdimensional Cable", "TV", "unknown"),
        Location(7, "Immortality Field Resort", "Resort", "unknown"),
        Location(8, "Post-Apocalyptic Earth", "Planet", "Post-Apocalyptic Dimension"),
        Location(9, "Purge Planet", "Planet", "Replacement Dimension"),
        Location(10, "Venzenulon 7", "Planet", "unknown")
    )
}

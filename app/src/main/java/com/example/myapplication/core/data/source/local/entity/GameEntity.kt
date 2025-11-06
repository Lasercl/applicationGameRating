package com.example.myapplication.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val gamesId: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "description")
    val description: String?,


    @ColumnInfo(name = "rating")
    val rating: Double?,

    @ColumnInfo(name = "ratingsCount")
    val ratingsCount: Int?,

    @ColumnInfo("released")
    val released: String? = null,

    @ColumnInfo("updated")
    val updated: String? = null,

    @ColumnInfo(name = "image")
    val image: String?,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)
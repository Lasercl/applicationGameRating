package com.example.myapplication.core.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Game(
    val id: Int?,
    val name: String?,
    val backgroundImage: String?,
    val released: String?,
    val rating: Double?,
    val ratingsCount: Int?,
    val description: String? = null,
    val isFavorite: Boolean = false
) : Parcelable
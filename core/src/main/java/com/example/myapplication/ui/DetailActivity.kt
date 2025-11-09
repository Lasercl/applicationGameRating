package com.example.gamesapp.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myapplication.core.R
import com.example.myapplication.ui.DetailViewModel
import com.example.myapplication.core.data.Resource
import com.example.myapplication.core.data.domain.model.Game
import com.example.myapplication.core.databinding.ActivityDetailBinding

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailGameViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // nonaktifin title bawaan
        binding.toolbar.title = ""

        // Ambil ID dari Intent
        val game:Game = getParcelableExtra(intent, EXTRA_GAME, Game::class.java) as Game
        val gameId=game.id!!

        if (gameId != -1) {
            observeGameDetail(gameId)
        }
    }

    private fun observeGameDetail(id: Int) {
        lifecycleScope.launch {
            detailGameViewModel.getGameDetail(id).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = android.view.View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = android.view.View.GONE
                        resource.data?.let { showGameDetail(it) }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = android.view.View.GONE
                        binding.tvError.text = resource.message
                        binding.tvError.visibility = android.view.View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showGameDetail(game: Game) {
        supportActionBar?.title = game.name//        binding.contentDetail.tvDetailDescription.text = game.description ?: "No description available."
        binding.toolbar.title =game.name
        val htmlContent = game.description ?: "<p>No description available.</p>"
        binding.contentDetail.tvDetailDescription.loadDataWithBaseURL(
            null,
            htmlContent,
            "text/html",
            "utf-8",
            null
        )
        binding.contentDetail.tvRating.text= "Rating: ${game.rating ?: "-"}"
        binding.contentDetail.tvReleased.text = "Released: ${game.released ?: "-"}"

        Glide.with(this)
            .load(game.backgroundImage)
            .into(binding.ivDetailImage)

        var statusFavorite = game.isFavorite
        setStatusFavorite(statusFavorite)

        binding.fab.setOnClickListener {
            statusFavorite = !statusFavorite
            detailGameViewModel.setFavoriteGames(game, statusFavorite)
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_full))
        } else {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star))
        }
    }

    companion object {
        const val EXTRA_GAME = "extra_game"
    }
}

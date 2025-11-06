package com.example.myapplication.ui.event

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.ViewModelFactory
import com.example.myapplication.adapter.ListGamesAdapter
import com.example.myapplication.databinding.FragmentListEventBinding
import com.example.gamesapp.detail.DetailActivity
import com.example.myapplication.core.data.Resource
import com.example.myapplication.core.data.domain.model.Game

class ListGamesFragment : Fragment() {
    private lateinit var listGamesViewModel :ListGamesViewModel
    private var _binding: FragmentListEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListEventBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity())
        val gamesAdapter = ListGamesAdapter()
        gamesAdapter.setOnItemClickListener { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_GAME, selectedData)
            startActivity(intent)

        }

        listGamesViewModel = ViewModelProvider(this, factory)[ListGamesViewModel::class.java]


        listGamesViewModel.game.observe(viewLifecycleOwner) { games ->
            if (games != null) {
                when (games) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        gamesAdapter.submitList(games.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            games.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        }
        with(binding.rvHeroes) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = gamesAdapter
        }

    }



}
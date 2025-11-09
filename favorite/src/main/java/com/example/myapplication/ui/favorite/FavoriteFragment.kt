package com.example.myapplication.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamesapp.detail.DetailActivity
import com.example.myapplication.di.favoriteModule
import com.example.myapplication.ui.ListGamesAdapter
import com.example.myapplication.favorite.databinding.FragmentFavoriteEventBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [FavoriteEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    private val listGamesViewModel : FavoriteViewModel by viewModel()

    private var _binding: FragmentFavoriteEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteEventBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(favoriteModule)

        val gamesAdapter = ListGamesAdapter()
        gamesAdapter.setOnItemClickListener { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_GAME, selectedData)
            startActivity(intent)

        }



        listGamesViewModel.game.observe(viewLifecycleOwner) { games ->
            if (games != null) {
                gamesAdapter.submitList(games)

                }
            }
        with(binding.rvHeroes) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = gamesAdapter
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
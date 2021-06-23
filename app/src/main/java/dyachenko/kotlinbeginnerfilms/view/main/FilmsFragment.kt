package dyachenko.kotlinbeginnerfilms.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.FilmsFragmentBinding
import dyachenko.kotlinbeginnerfilms.hide
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.show
import dyachenko.kotlinbeginnerfilms.showSnackBar
import dyachenko.kotlinbeginnerfilms.view.details.FilmFragment
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.FilmsViewModel

class FilmsFragment : Fragment() {
    private var _binding: FilmsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmsViewModel by lazy {
        ViewModelProvider(this).get(FilmsViewModel::class.java)
    }

    private val adapter = FilmsAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(film: Film) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, FilmFragment.newInstance(film))
                    .addToBackStack(null)
                    .commit()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilmsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filmsRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner, observer)
            getFilmFromLocalSource()
        }
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                loadingLayout.hide()
                adapter.setFilms(appState.films)
                filmsRootView.showSnackBar(R.string.success_msg)
            }
            is AppState.Loading -> {
                loadingLayout.show()
            }
            is AppState.Error -> {
                loadingLayout.hide()
                filmsRootView.showSnackBar(R.string.error_msg, R.string.reload_msg,
                    { viewModel.getFilmFromLocalSource() })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(film: Film)
    }

    companion object {
        fun newInstance() = FilmsFragment()
    }
}
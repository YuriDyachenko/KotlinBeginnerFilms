package dyachenko.kotlinbeginnerfilms.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.FilmFragmentBinding
import dyachenko.kotlinbeginnerfilms.hide
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.show
import dyachenko.kotlinbeginnerfilms.showSnackBar
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.FilmViewModel

class FilmFragment : Fragment() {
    private var _binding: FilmFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmViewModel by lazy {
        ViewModelProvider(this).get(FilmViewModel::class.java)
    }

    private val filmId: Int by lazy {
        arguments?.getInt(ARG_FILM_ID)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        getData()
    }

    private fun getData() {
        viewModel.getFilmFromServer(filmId)
    }

    private fun setData(film: Film) = with(binding) {
        with(film) {
            val text = "$title\n$id\n$overview\n$poster_path\n$popularity\n$adult"
            filmDetailsTextView.text = text
        }
    }

    private fun renderData(appState: AppState?) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                filmLoadingLayout.hide()
                setData(appState.films[0])
            }
            is AppState.Loading -> {
                filmLoadingLayout.show()
            }
            is AppState.Error -> {
                filmLoadingLayout.hide()
                filmRootView.showSnackBar(appState.error.message!!, getString(R.string.reload_msg), { getData() })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FILM_ID = "ARG_FILM_ID"

        fun newInstance(filmId: Int) =
            FilmFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FILM_ID, filmId)
                }
            }
    }
}
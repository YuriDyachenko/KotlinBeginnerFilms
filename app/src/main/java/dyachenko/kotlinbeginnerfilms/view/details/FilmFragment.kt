package dyachenko.kotlinbeginnerfilms.view.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.FilmFragmentBinding
import dyachenko.kotlinbeginnerfilms.hide
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource.Companion.IMAGE_SITE
import dyachenko.kotlinbeginnerfilms.show
import dyachenko.kotlinbeginnerfilms.showSnackBar
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.FilmViewModel

class FilmFragment : Fragment() {
    private var _binding: FilmFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmViewModel by lazy {
        ViewModelProvider(this).get(FilmViewModel::class.java)
    }

    private val filmId: Int by lazy {
        arguments?.getInt(ARG_FILM_ID) ?: NO_ID
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
        viewModel.resourceProvider = ResourceProvider(requireContext())
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        getData()
    }

    private fun getData() {
        viewModel.getFilmFromServer(filmId)
    }

    private fun setData(film: Film) = with(binding) {
        with(film) {
            val text = "$title\n$overview\n\n$popularity\n$adult"
            filmDetailsTextView.text = text
            Picasso
                .get()
                .load("${IMAGE_SITE}$poster_path")
                .into(filmDetailsImageView)
        }
    }

    private fun renderData(appState: AppState?) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                filmLoadingLayout.hide()
                setData(appState.films.first())
            }
            is AppState.Loading -> {
                filmLoadingLayout.show()
            }
            is AppState.Error -> {
                filmLoadingLayout.hide()
                filmRootView.showSnackBar(appState.error.message ?: getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { getData() })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_settings).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val ARG_FILM_ID = "ARG_FILM_ID"
        const val NO_ID = 0

        fun newInstance(filmId: Int) =
            FilmFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FILM_ID, filmId)
                }
            }
    }
}
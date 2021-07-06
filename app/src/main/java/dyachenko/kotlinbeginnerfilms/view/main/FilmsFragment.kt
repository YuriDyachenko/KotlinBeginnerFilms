package dyachenko.kotlinbeginnerfilms.view.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.*
import dyachenko.kotlinbeginnerfilms.databinding.FilmsFragmentBinding
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.FilmsListType
import dyachenko.kotlinbeginnerfilms.model.FilmsListTypeChanging
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import dyachenko.kotlinbeginnerfilms.view.details.FilmFragment
import dyachenko.kotlinbeginnerfilms.view.history.HistoryFragment
import dyachenko.kotlinbeginnerfilms.view.settings.Settings
import dyachenko.kotlinbeginnerfilms.view.settings.SettingsFragment
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
                    .add(R.id.container, FilmFragment.newInstance(film.id!!))
                    .addToBackStack(null)
                    .commit()
            }
        }
    })

    private val onListTypeChanging = object : FilmsListTypeChanging {
        override fun changed() {
            getData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilmsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSettings()
        binding.filmsRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.resourceProvider = ResourceProvider(requireContext())
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        getData()
    }

    private fun readSettings() {
        activity?.let {
            val typeOrdinal =
                it.getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE)
                    .getInt(Settings.FILMS_LIST_TYPE_NAME, FilmsListType.POPULAR.ordinal)
            Settings.FILMS_LIST_TYPE = when (typeOrdinal) {
                FilmsListType.TOP_RATED.ordinal -> FilmsListType.TOP_RATED
                FilmsListType.UPCOMING.ordinal -> FilmsListType.UPCOMING
                FilmsListType.NOW_PLAYING.ordinal -> FilmsListType.NOW_PLAYING
                else -> FilmsListType.POPULAR
            }
        }
    }

    private fun getData() {
        viewModel.getFilmsFromServer()
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                filmsLoadingLayout.hide()
                filmsTypeTextView.text = getString(Settings.FILMS_LIST_TYPE.rId)
                adapter.setFilms(appState.films)
            }
            is AppState.Loading -> {
                filmsLoadingLayout.show()
            }
            is AppState.Error -> {
                filmsLoadingLayout.hide()
                filmsRootView.showSnackBar(appState.error.message ?: getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { getData() })
            }
            else -> {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                activity?.supportFragmentManager?.addFragmentWithBackStack(
                    SettingsFragment.newInstance(
                        onListTypeChanging
                    )
                )
                true
            }
            R.id.action_history -> {
                activity?.supportFragmentManager?.addFragmentWithBackStack(
                    HistoryFragment.newInstance()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance() = FilmsFragment()
    }
}
package dyachenko.kotlinbeginnerfilms.view.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.*
import dyachenko.kotlinbeginnerfilms.databinding.FilmsFragmentBinding
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.FilmsListType
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import dyachenko.kotlinbeginnerfilms.view.contacts.ContactsFragment
import dyachenko.kotlinbeginnerfilms.view.details.FilmFragment
import dyachenko.kotlinbeginnerfilms.view.history.HistoryFragment
import dyachenko.kotlinbeginnerfilms.view.settings.Settings
import dyachenko.kotlinbeginnerfilms.view.settings.SettingsFragment
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.FilmsViewModel

class FilmsFragment : Fragment() {
    private var _binding: FilmsFragmentBinding? = null
    private val binding get() = _binding!!

    private var isUploading = false

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

    }, object : OnNeedLoadNewPageListener {

        override fun onNeedLoadNewPage() {
            isUploading = true
            getData()
        }

    })

    private val onSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            readSettings()
            getData()
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
        activity?.getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE)
            ?.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)

        binding.filmsRecyclerView.adapter = adapter
        binding.filmsRecyclerView.setHasFixedSize(true)

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
            Settings.FILMS_LIST_TYPE = FilmsListType.byOrdinal(typeOrdinal)
        }
    }

    private fun getData() {
        viewModel.getFilmsFromServer(isUploading)
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                isUploading = false
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
        activity?.getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE)
            ?.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        adapter.removeListeners()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(film: Film)
    }

    interface OnNeedLoadNewPageListener {
        fun onNeedLoadNewPage()
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
                activity?.showFragment(SettingsFragment.newInstance())
                return true
            }
            R.id.action_history -> {
                activity?.showFragment(HistoryFragment.newInstance())
                return true
            }
            R.id.action_contacts -> {
                activity?.showFragment(ContactsFragment.newInstance())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance() = FilmsFragment()
    }
}
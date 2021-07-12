package dyachenko.kotlinbeginnerfilms.view.notes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.*
import dyachenko.kotlinbeginnerfilms.databinding.NotesFragmentBinding
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.NotesViewModel

class NotesFragment : Fragment() {
    private var _binding: NotesFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by lazy {
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    private val adapter = NotesAdapter()

    private val filmId: Int by lazy {
        arguments?.getInt(ARG_FILM_ID) ?: NO_ID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notesRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        getData()
    }

    private fun getData() {
        viewModel.getNotesByFilmId(filmId)
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.SuccessNotes -> {
                notesLoadingLayout.hide()
                adapter.setNoteEntities(appState.noteEntities)
            }
            is AppState.Loading -> {
                notesLoadingLayout.show()
            }
            is AppState.Error -> {
                notesLoadingLayout.hide()
                notesRootView.showSnackBar(appState.error.message
                    ?: getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { getData() })
            }
            else -> {
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
        menu.hideAllItems()
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val ARG_FILM_ID = "ARG_FILM_ID"
        const val NO_ID = 0

        fun newInstance(filmId: Int) = NotesFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_FILM_ID, filmId)
            }
        }
    }
}
package dyachenko.kotlinbeginnerfilms.view.note

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.NoteFragmentBinding
import dyachenko.kotlinbeginnerfilms.hideItems
import dyachenko.kotlinbeginnerfilms.viewmodel.NoteViewModel

class NoteFragment : Fragment() {
    private var _binding: NoteFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private val filmId: Int? by lazy {
        arguments?.getInt(ARG_FILM_ID)
    }

    private val filmTitle: String? by lazy {
        arguments?.getString(ARG_FILM_TITLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        saveButton.setOnClickListener {
            writeSettings()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun writeSettings() {
        viewModel.saveNoteToDB(filmId, filmTitle, binding.noteEditText.text.toString())
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
        menu.hideItems(
            R.id.action_settings,
            R.id.action_history,
            R.id.action_add_note,
            R.id.action_notes
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        const val ARG_FILM_ID = "ARG_FILM_ID"
        const val ARG_FILM_TITLE = "ARG_FILM_TITLE"

        fun newInstance(filmId: Int, filmTitle: String?) = NoteFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_FILM_ID, filmId)
                putString(ARG_FILM_TITLE, filmTitle)
            }
        }
    }
}
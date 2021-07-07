package dyachenko.kotlinbeginnerfilms.view.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dyachenko.kotlinbeginnerfilms.databinding.ErrorFragmentBinding

class ErrorFragment : Fragment() {
    private var _binding: ErrorFragmentBinding? = null
    private val binding get() = _binding!!

    private val text: String by lazy {
        arguments?.getString(ARG_TEXT, EMPTY_TEXT) ?: EMPTY_TEXT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ErrorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        errorTextView.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        super.onCreate(savedInstanceState)
    }

    companion object {
        private const val ARG_TEXT = "ARG_TEXT"
        private const val EMPTY_TEXT = ""

        fun newInstance(text: String) = ErrorFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TEXT, text)
            }
        }
    }
}
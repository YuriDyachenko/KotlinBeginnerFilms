package dyachenko.kotlinbeginnerfilms.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dyachenko.kotlinbeginnerfilms.databinding.FilmFragmentBinding
import dyachenko.kotlinbeginnerfilms.model.Film

class FilmFragment : Fragment() {
    private var _binding: FilmFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        arguments?.getParcelable<Film>(ARG_FILM)?.let { film ->
            with(film) {
                val text = "$title\n$id\n$overview\n$poster_path\n$popularity\n$adult"
                binding.filmDetailsTextView.text = text
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FILM = "ARG_FILM"

        fun newInstance(film: Film?) =
            FilmFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FILM, film)
                }
            }
    }
}
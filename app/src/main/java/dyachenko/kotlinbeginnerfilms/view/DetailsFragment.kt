package dyachenko.kotlinbeginnerfilms.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dyachenko.kotlinbeginnerfilms.databinding.FragmentDetailsBinding
import dyachenko.kotlinbeginnerfilms.model.Film

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var filmData: Film? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmData = it.getSerializable(ARG_FILM) as Film?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val text = filmData?.title + "\n" +
                filmData?.overview + "\n" +
                filmData?.poster_path + "\n" +
                filmData?.popularity.toString() + "\n" +
                filmData?.adult.toString()
        binding.filmDetailsTextView.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_FILM = "ARG_FILM"

        @JvmStatic
        fun newInstance(filmData: Film?) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_FILM, filmData)
                }
            }
    }
}
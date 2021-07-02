package dyachenko.kotlinbeginnerfilms.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.FilmFragmentBinding
import dyachenko.kotlinbeginnerfilms.hide
import dyachenko.kotlinbeginnerfilms.model.*
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
        arguments?.getInt(ARG_FILM_ID) ?: NO_ID
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.getStringExtra(LOAD_RESULT_EXTRA)) {
                    LOAD_RESULT_ERROR -> {
                        binding.filmLoadingLayout.hide()
                        binding.filmRootView.showSnackBar(it.getStringExtra(
                            LOAD_RESULT_DESCRIPTION_EXTRA
                        ) ?: getString(R.string.error_msg),
                            getString(R.string.reload_msg),
                            { startService(context) })
                    }
                    LOAD_RESULT_OK -> {
                        binding.filmLoadingLayout.hide()
                        val film = it.getSerializableExtra(FILM_EXTRA) as Film
                        setData(film)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(receiver, IntentFilter(FILM_INTENT_FILTER))
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)
        }
        super.onDestroy()
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

        /*
        ВЕРНУ обратно в следующей домашке
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        getData()
        */
        startService(context)
    }

    private fun startService(context: Context?) {
        context?.let {
            binding.filmLoadingLayout.show()
            it.startService(Intent(it, FilmService::class.java).apply {
                putExtra(FILM_ID_EXTRA, filmId)
            })
        }
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
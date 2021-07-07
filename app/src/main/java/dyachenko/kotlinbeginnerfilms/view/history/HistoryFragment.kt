package dyachenko.kotlinbeginnerfilms.view.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.*
import dyachenko.kotlinbeginnerfilms.databinding.HistoryFragmentBinding
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        getData()
    }

    private fun getData() {
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.SuccessHistory -> {
                historyLoadingLayout.hide()
                adapter.setHistoryEntities(appState.historyEntities)
            }
            is AppState.Loading -> {
                historyLoadingLayout.show()
            }
            is AppState.Error -> {
                historyLoadingLayout.hide()
                historyRootView.showSnackBar(appState.error.message
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
        menu.hideItems(
            R.id.action_settings,
            R.id.action_history
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }
}
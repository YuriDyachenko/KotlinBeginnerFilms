package dyachenko.kotlinbeginnerfilms.view.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dyachenko.kotlinbeginnerfilms.*
import dyachenko.kotlinbeginnerfilms.databinding.ContactsFragmentBinding
import dyachenko.kotlinbeginnerfilms.viewmodel.AppState
import dyachenko.kotlinbeginnerfilms.viewmodel.ContactsViewModel

class ContactsFragment : Fragment() {
    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this).get(ContactsViewModel::class.java)
    }

    private val adapter = ContactsAdapter()

    private val requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getData()
            } else {
                binding.contactsRootView.showSnackBar(getString(R.string.permission_error_msg),
                    getString(R.string.permission_reload_msg),
                    { checkPermission() })
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactsRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        checkPermission()
    }

    private fun requestPermission() {
        requestLauncher.launch(REQUEST)
    }

    private fun checkPermission() {
        context?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, REQUEST) -> {
                    getData()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun getData() {
        viewModel.getAllContacts(context)
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.SuccessContacts -> {
                contactsLoadingLayout.hide()
                adapter.setContacts(appState.contacts)
            }
            is AppState.Loading -> {
                contactsLoadingLayout.show()
            }
            is AppState.Error -> {
                contactsLoadingLayout.hide()
                contactsRootView.showSnackBar(appState.error.message
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
            R.id.action_history,
            R.id.action_contacts
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val REQUEST = Manifest.permission.READ_CONTACTS

        fun newInstance() = ContactsFragment()
    }
}
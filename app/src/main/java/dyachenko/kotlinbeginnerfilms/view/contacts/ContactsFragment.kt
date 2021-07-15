package dyachenko.kotlinbeginnerfilms.view.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

    private var phoneToDial: String? = null

    private val adapter = ContactsAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(phone: String?) {
            phoneToDial = phone
            checkCallsPermission()
        }
    })

    private val requestContactsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getData()
            } else {
                binding.contactsRootView.showSnackBar(getString(R.string.permission_error_msg),
                    getString(R.string.permission_reload_msg),
                    { checkContactsPermission() })
            }
        }

    private val requestCallsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                phoneCall()
            } else {
                binding.contactsRootView.showSnackBar(getString(R.string.permission_error_msg),
                    getString(R.string.permission_reload_msg),
                    { checkContactsPermission() })
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
        checkContactsPermission()
    }

    private fun requestContactsPermission() {
        requestContactsLauncher.launch(REQUEST_CONTACTS)
    }

    private fun requestCallsPermission() {
        requestCallsLauncher.launch(REQUEST_CALLS)
    }

    private fun checkContactsPermission() {
        context?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, REQUEST_CONTACTS) -> {
                    getData()
                }
                else -> {
                    requestContactsPermission()
                }
            }
        }
    }

    private fun checkCallsPermission() {
        context?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, REQUEST_CALLS) -> {
                    phoneCall()
                }
                else -> {
                    requestCallsPermission()
                }
            }
        }
    }

    private fun phoneCall() {
        val phone = phoneToDial ?: return
        val toDial = "tel:$phone"
        val intent = Intent(Intent.ACTION_CALL, Uri.parse(toDial))
        try {
            startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
            activity?.showError(e)
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
        adapter.removeListeners()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(phone: String?)
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
        private const val REQUEST_CONTACTS = Manifest.permission.READ_CONTACTS
        private const val REQUEST_CALLS = Manifest.permission.CALL_PHONE

        fun newInstance() = ContactsFragment()
    }
}
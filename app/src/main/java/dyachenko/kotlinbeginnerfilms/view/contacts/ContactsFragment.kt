package dyachenko.kotlinbeginnerfilms.view.contacts

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
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

    private lateinit var requestContactsLauncher: ActivityResultLauncher<String>
    private lateinit var requestCallsLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)

        requestContactsLauncher = registerPermissionLauncher(
            { getData() },
            { checkContactsPermission() },
            binding.contactsRootView
        )

        requestCallsLauncher = registerPermissionLauncher(
            { phoneCall() },
            { checkCallsPermission() },
            binding.contactsRootView
        )

        return binding.root
    }

    private fun checkContactsPermission() {
        checkPermission(Manifest.permission.READ_CONTACTS, { getData() }, requestContactsLauncher)
    }

    private fun checkCallsPermission() {
        checkPermission(Manifest.permission.CALL_PHONE, { phoneCall() }, requestCallsLauncher)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactsRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        checkContactsPermission()
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
        fun newInstance() = ContactsFragment()
    }
}
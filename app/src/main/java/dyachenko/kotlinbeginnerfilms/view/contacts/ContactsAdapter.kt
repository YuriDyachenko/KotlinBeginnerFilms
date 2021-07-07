package dyachenko.kotlinbeginnerfilms.view.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.ContactsItemBinding
import dyachenko.kotlinbeginnerfilms.model.Contact

class ContactsAdapter(
    private var onItemViewClickListener: ContactsFragment.OnItemViewClickListener?
) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private var contacts: List<Contact> = listOf()

    fun setContacts(list: List<Contact>) {
        contacts = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount() = contacts.size

    fun removeListeners() {
        onItemViewClickListener = null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ContactsItemBinding.bind(view)

        fun bind(contact: Contact) = with(binding) {
            itemView.apply {
                val text = "${contact.name}: ${hidePhoneNumber(contact.phone)}"
                contactsItemTextView.text = text

                setOnClickListener { onItemViewClickListener?.onItemViewClick(contact.phone) }
            }
        }
    }

    private fun hidePhoneNumber(phone: String?): String? {
        phone?.let {
            return if (HIDE_PHONE_NUMBER) {
                StringBuilder(
                    if (phone.length >= 5) {
                        phone.substring(0, 5)
                    } else {
                        phone
                    }
                ).append(HIDE_VALUE).toString()
            } else {
                phone
            }
        } ?: return phone
    }

    companion object {
        private const val HIDE_PHONE_NUMBER = true
        private const val HIDE_VALUE = " ..."
    }
}
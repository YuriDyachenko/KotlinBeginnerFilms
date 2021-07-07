package dyachenko.kotlinbeginnerfilms.viewmodel

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.model.Contact
import kotlinx.coroutines.*

class ContactsViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getLiveData() = liveDataToObserve

    fun getAllContacts(context: Context?) {
        liveDataToObserve.value = AppState.Loading
        launch {
            val job = async(Dispatchers.IO) {
                val list = mutableListOf<Contact>()
                context?.let {
                    val columnName = ContactsContract.Contacts.DISPLAY_NAME
                    val cursorWithContacts = it.contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        "$columnName ASC"
                    )
                    cursorWithContacts?.let { cursor ->
                        val columnNameIndex = cursor.getColumnIndex(columnName)
                        for (i in 0..cursor.count) {
                            if (cursor.moveToPosition(i)) {
                                val name = cursor.getString(columnNameIndex)
                                list.add(Contact(name))
                            }
                        }
                    }
                    cursorWithContacts?.close()
                }
                list
            }
            liveDataToObserve.value = AppState.SuccessContacts(job.await())
        }
    }
}
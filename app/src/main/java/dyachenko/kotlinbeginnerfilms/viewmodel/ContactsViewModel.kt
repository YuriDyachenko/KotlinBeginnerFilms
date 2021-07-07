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
                    val columnName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    val columnPhone = ContactsContract.CommonDataKinds.Phone.DATA1
                    val cursorWithContacts = it.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        "$columnName ASC"
                    )
                    cursorWithContacts?.let { cursor ->
                        val columnNameIndex = cursor.getColumnIndex(columnName)
                        val columnPhoneIndex = cursor.getColumnIndex(columnPhone)
                        for (i in 0..cursor.count) {
                            if (cursor.moveToPosition(i)) {
                                val name = cursor.getString(columnNameIndex)
                                val phone = cursor.getString(columnPhoneIndex)
                                list.add(Contact(name, phone))
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
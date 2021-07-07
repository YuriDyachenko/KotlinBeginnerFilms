package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.PageDTO
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource.Companion.FILMS_ON_PAGE
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource.Companion.FIRST_PAGE
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmsViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var resourceProvider: ResourceProvider? = null
    private val list: MutableList<Film> = mutableListOf()

    private val callbackForGetPage = object : Callback<PageDTO> {

        override fun onResponse(call: Call<PageDTO>, response: Response<PageDTO>) {
            val page = response.body()
            if (response.isSuccessful && page != null) {
                list.addAll(page.results.toList())
                liveDataToObserve.value = AppState.Success(list)
            } else {
                liveDataToObserve.value =
                    AppState.Error(Throwable(getString(R.string.error_server_msg)))
            }
        }

        override fun onFailure(call: Call<PageDTO>, t: Throwable) {
            liveDataToObserve.value = AppState.Error(
                Throwable(t.message ?: getString(R.string.error_request_msg))
            )
        }
    }

    fun getLiveData() = liveDataToObserve

    fun getFilmsFromServer(isUploading: Boolean) {
        if (!isUploading) {
            list.clear()
        }
        val pageNumber = list.size / FILMS_ON_PAGE + FIRST_PAGE
        liveDataToObserve.value = AppState.Loading
        RemoteDataSource().getNextPage(pageNumber, callbackForGetPage)
    }

    private fun getString(id: Int) = resourceProvider?.getString(id)
}
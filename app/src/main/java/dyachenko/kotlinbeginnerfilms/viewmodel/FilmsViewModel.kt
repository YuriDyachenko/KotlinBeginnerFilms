package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.model.Film
import dyachenko.kotlinbeginnerfilms.model.PageDTO
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource.Companion.FIRST_PAGE
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource.Companion.MAX_PAGE
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.min

class FilmsViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var resourceProvider: ResourceProvider? = null
    private val list: MutableList<Film> = mutableListOf()

    fun getPageFromServer(pageNumber: Int) {
        val callback = object : Callback<PageDTO> {
            override fun onResponse(call: Call<PageDTO>, response: Response<PageDTO>) {
                val page = response.body()
                if (response.isSuccessful && page != null) {
                    list.addAll(page.results.toList())
                    val pagesToRead = min(page.total_pages ?: FIRST_PAGE, MAX_PAGE)
                    if (pageNumber < pagesToRead) {
                        getPageFromServer(pageNumber + 1)
                    } else {
                        liveDataToObserve.value = AppState.Success(list)
                    }
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
        if (pageNumber == FIRST_PAGE) {
            RemoteDataSource().getFirstPage(callback)
        } else {
            RemoteDataSource().getNextPage(pageNumber, callback)
        }
    }

    fun getLiveData() = liveDataToObserve

    fun getFilmsFromServer() {
        liveDataToObserve.value = AppState.Loading
        getPageFromServer(FIRST_PAGE)
    }

    private fun getString(id: Int) = resourceProvider?.getString(id)
}
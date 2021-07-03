package dyachenko.kotlinbeginnerfilms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.model.PageDTO
import dyachenko.kotlinbeginnerfilms.model.RemoteDataSource
import dyachenko.kotlinbeginnerfilms.view.ResourceProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmsViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var resourceProvider: ResourceProvider? = null

    private val callback = object : Callback<PageDTO> {
        override fun onResponse(call: Call<PageDTO>, response: Response<PageDTO>) {
            val page = response.body()
            liveDataToObserve.value = if (response.isSuccessful && page != null) {
                AppState.Success(page.results.toList())
            } else {
                AppState.Error(Throwable(getString(R.string.error_server_msg)))
            }
        }

        override fun onFailure(call: Call<PageDTO>, t: Throwable) {
            liveDataToObserve.value = AppState.Error(
                Throwable(
                    t.message
                        ?: getString(R.string.error_request_msg)
                )
            )
        }
    }

    fun getLiveData() = liveDataToObserve

    fun getFilmsFromServer() {
        liveDataToObserve.value = AppState.Loading
        RemoteDataSource().getFirstPage(callback)
    }

    private fun getString(id: Int) = resourceProvider?.getString(id)
}
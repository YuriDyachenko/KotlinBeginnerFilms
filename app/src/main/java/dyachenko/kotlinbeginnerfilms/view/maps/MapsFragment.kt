package dyachenko.kotlinbeginnerfilms.view.maps

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.databinding.MapsFragmentBinding
import dyachenko.kotlinbeginnerfilms.hideAllItems
import kotlinx.coroutines.*
import java.io.IOException

class MapsFragment : Fragment(), CoroutineScope by MainScope() {
    private var _binding: MapsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapLongClickListener { latLng -> getAddressAsync(latLng) }
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            launch {
                val job = async(Dispatchers.IO) {
                    geoCoder.getFromLocation(
                        location.latitude,
                        location.longitude, 1
                    )
                }
                try {
                    val addresses = job.await()
                    if (addresses.size > 0) {
                        binding.addressTextView.post {
                            binding.addressTextView.text = addresses[0].getAddressLine(0)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }

    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = binding.searchAddressEditText.text.toString()
            launch {
                val job = async(Dispatchers.IO) {
                    geoCoder.getFromLocationName(searchText, 1)
                }
                try {
                    val addresses = job.await()
                    if (addresses.size > 0) {
                        goToAddress(addresses, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun goToAddress(addresses: MutableList<Address>, view: View, searchText: String) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            setMarker(location, searchText)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    private fun setMarker(location: LatLng, searchText: String) {
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )
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
        menu.hideAllItems()
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}
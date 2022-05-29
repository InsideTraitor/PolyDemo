package hollis.inman.polydemo.ui.main.map

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import hollis.inman.polydemo.BaseActivity
import hollis.inman.polydemo.R
import hollis.inman.polydemo.databinding.FragmentMapsBinding
import hollis.inman.polydemo.ui.main.utils.PermissionsHelper


class MapsFragment : Fragment() {

    companion object {
        fun newInstance() = MapsFragment()
    }

    lateinit var viewModel: MapViewModel
    lateinit var binding: FragmentMapsBinding
    lateinit var map: GoogleMap
    lateinit var fusedLocationProvider: FusedLocationProviderClient
    lateinit var sharedPreferences: SharedPreferences


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        PermissionsHelper.enableMyLocation(activity as AppCompatActivity, activity?.supportFragmentManager!!, map)
        map.setOnMapClickListener { viewModel.onSetFencePost(map, it) }
        fusedLocationProvider.lastLocation.addOnSuccessListener { if (it != null) viewModel.setCameraPosition(map, LatLng(it.latitude, it.longitude), 15f) }
        viewModel.loadSavedAreaAndMarkers(map)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindViews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        viewModel.listener = context as BaseActivity
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    fun init() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        setClickListeners()
        setFusedLocationProviderClient()
        viewModel.initSharedPreferences(sharedPreferences)

    }

    fun setFusedLocationProviderClient() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context as BaseActivity)
    }

    fun setClickListeners() {
        binding.btnSavePolygon.setOnClickListener { viewModel.onSaveMapButtonClicked(map) }
        binding.btnDeletePolygon.setOnClickListener { viewModel.onDeleteMapButtonClick(map) }
        binding.btnEditProfile.setOnClickListener { viewModel.onEditProfileNavigationClick() }
    }

    fun bindViews() {
        binding = FragmentMapsBinding.inflate(layoutInflater)
    }
}
package hollis.inman.polydemo.ui.main.map

import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import hollis.inman.polydemo.BaseActivity
import hollis.inman.polydemo.R
import hollis.inman.polydemo.databinding.FragmentMapsBinding
import hollis.inman.polydemo.ui.main.profile.edit.EditProfileViewModel2
import hollis.inman.polydemo.ui.main.profile.view.ProfileFragment

class MapsFragment : Fragment() {

    companion object {
        fun newInstance() = MapsFragment()
    }

    lateinit var viewModel: MapViewModel
    lateinit var binding: FragmentMapsBinding
    lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        map = googleMap
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map.setOnMapClickListener { viewModel.onSetFencePost(map, it) }
//        val polygon1 = map.addPolygon(
//            PolygonOptions()
//            .clickable(true)
//            .add(
//                LatLng(-27.457, 153.040),
//                LatLng(-33.852, 151.211),
//                LatLng(-37.813, 144.962),
//                LatLng(-34.928, 138.599)))
//        stylePolygon(polygon1)
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
        setClickListeners()
    }

    fun setClickListeners() {
        binding.btnSaveArea.setOnClickListener { viewModel.onSaveMapButtonClicked() }

    }

    fun bindViews() {
        binding = FragmentMapsBinding.inflate(layoutInflater)
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun stylePolygon(polygon: Polygon) {
        // Get the data object stored with the polygon.
        val type = polygon.tag?.toString() ?: ""
        var pattern: List<PatternItem>? = null
        var strokeColor = resources.getColor(R.color.colorPrimary, resources.newTheme())
        var fillColor = resources.getColor(R.color.white)
        polygon.strokePattern = pattern
        polygon.strokeWidth = 5f
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }

}
package hollis.inman.polydemo.ui.main.map

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import hollis.inman.polydemo.ui.main.map.subunits.MapsListener
import kotlin.random.Random

class MapViewModel: ViewModel() {

    /**
     * Map variables
     */
    lateinit var listener: MapsListener
    lateinit var polygon: PolygonOptions
    var polyPoints: MutableList<LatLng> = mutableListOf()
    var fillColor = Color.HSVToColor(35, floatArrayOf(110.toFloat(), 1f, 1f))
    lateinit var cameraPosition: CameraPosition
    lateinit var latLngBounds: LatLngBounds
    val minPolyPoints = 2
    val maxPolyPoints = 4

    /**
     * Add Polygon to map
     */
    fun addPolygonToMap(map: GoogleMap) {
        polygon = PolygonOptions()
            .clickable(true)
            .fillColor(fillColor)
            .strokeWidth(0f)
            .addAll(polyPoints)
//        map.clear()
        map.addPolygon(
            polygon
        )
    }

    /**
     * Delete Polygon
     */
    fun clearMap(map: GoogleMap) {
        map.clear()
    }

    /**
     * Handle map delete button click
     */
    fun onDeleteMapButtonClick(map: GoogleMap) {
        clearMap(map)
        polyPoints.clear()
    }

    /**
     * Save polygon
     */
    fun onSaveMapButtonClicked() {
        if (polyPoints.size > minPolyPoints) {
            listener.onSaveMapButtonClicked()
        } else {
            listener.errorTooFewPoints()
        }
    }

    /**
     * Return to edit user profile
     */
    fun onEditProfileNavigationClick() {
        listener.onEditProfileNavigationClick()
    }

    /**
     * Load saved polygon
     */
    fun loadPolygon(polygon: PolygonOptions, map: GoogleMap) {
        map.addPolygon(polygon)
    }

    /**
     * Check to see if a polygon contains a point
     */
    fun polygonContainsPoint(polygonPoints: MutableList<LatLng>, pointToCheck: LatLng): Boolean {
        return false
    }

    /**
     * Set Camera postion
     */
    fun setCameraPosition(map: GoogleMap, latLng: LatLng, zoom: Float) {
        cameraPosition = CameraPosition.fromLatLngZoom(latLng, zoom)
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), object : GoogleMap.CancelableCallback {
            override fun onCancel() {
                // Do nothing
            }

            override fun onFinish() {
                getLatLngBounds(map)
                addRandomJobMarkers(map, 10)
            }
        })
    }

    /**
     * Get LatLngBounds of Current map
     */
    fun getLatLngBounds(map: GoogleMap) : LatLngBounds {
        latLngBounds = map.getProjection()
            .getVisibleRegion().latLngBounds
        return latLngBounds
    }

    /**
     * Add Random job markers
     */
    fun addRandomJobMarkers(map: GoogleMap, count: Int) {
        getLatLngBounds(map)
        repeat(count) {map.addMarker(MarkerOptions()
            .position(generateRandomLatLngInBounds(getLatLngBounds(map))))}
    }

    /**
     * Generate random LatLng within bounds
     */
    fun generateRandomLatLngInBounds(bounds: LatLngBounds): LatLng {
        var randomLatLngInBounds: LatLng = LatLng(0.0,0.0)
        val northEast = bounds.northeast
        val southWest = bounds.southwest
        val northWest = LatLng(bounds.northeast.latitude, bounds.southwest.longitude)
        val southEast = LatLng(bounds.southwest.latitude, bounds.southwest.longitude)


        return LatLng(Random.nextDouble(southEast.latitude, northEast.latitude), Random.nextDouble(southEast.latitude, northEast.latitude))
    }

    /**
     * Add LatLng point to user's polygon
     */
    fun addLatLngToPolygon(latLng: LatLng) {
        polyPoints.add(latLng)
    }

    /**
     * Set new fence pin
     */
    fun onSetFencePost(map: GoogleMap, position: LatLng) {
        if (polyPoints.count() < maxPolyPoints) {
            map.addMarker(MarkerOptions().position(position).title(position.latitude.toString() + ", " +position.longitude.toString()))
            addLatLngToPolygon(position)
            addPolygonToMap(map)
        } else {
            listener.errorTooManyPoints()
        }

    }
}
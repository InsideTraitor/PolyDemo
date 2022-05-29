package hollis.inman.polydemo.ui.main.map

import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.ktx.utils.containsLocation
import hollis.inman.polydemo.BaseActivity.Companion.JOB_MARKERS
import hollis.inman.polydemo.BaseActivity.Companion.SERVICE_AREA
import hollis.inman.polydemo.ui.main.map.subunits.MapsListener
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.reflect.Type
import kotlin.random.Random


class MapViewModel: ViewModel() {

    /**
     * Map variables
     */
    val minPolyPoints = 2
    val maxPolyPoints = 4
    val gson: Gson = Gson()

    enum class MarkerType {FENCEPOST, JOB}

    lateinit var listener: MapsListener
    lateinit var polygon: PolygonOptions
    lateinit var cameraPosition: CameraPosition
    lateinit var latLngBounds: LatLngBounds
    lateinit var sharedPreferences: SharedPreferences

    var polyPoints: MutableList<LatLng> = mutableListOf()
    var jobMarkers: MutableList<MarkerOptions> = mutableListOf()
    var savedJobMarkers: MutableList<MarkerOptions> = mutableListOf()
    var fillColor = Color.HSVToColor(35, floatArrayOf(110.toFloat(), 1f, 1f))

    /**
     * Add Polygon to map
     */
    fun addPolygonToMap(map: GoogleMap) {
        polygon = PolygonOptions()
            .clickable(true)
            .fillColor(fillColor)
            .strokeWidth(0f)
            .addAll(polyPoints)
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
     * Add marker
     */
    fun addMarker(map: GoogleMap, position: LatLng, type: MarkerType) {
        when(type) {
            MarkerType.FENCEPOST ->
                map.addMarker(MarkerOptions().position(position).title(position.latitude.toString() + ", " +position.longitude.toString()))
            MarkerType.JOB ->
                map.addMarker(MarkerOptions().position(position).title("Some Really Cool Job"))
        }
    }

    /**
     * Remove marker
     */
    fun deleteMarker() {

    }

    /**
     * Handle map delete button click
     */
    fun onDeleteMapButtonClick(map: GoogleMap) {
        clearMap(map)
        polyPoints.clear()
        resetMarkers(map, jobMarkers)
    }

    /**
     * Save polygon
     */
    fun onSaveMapButtonClicked(map: GoogleMap) {
        if (polyPoints.size > minPolyPoints) {
            listener.onSaveMapButtonClicked()
            getJobsInsidePolygon()
            map.clear()
            loadPolygon(polygon, map)
            resetMarkers(map, savedJobMarkers)
            saveServiceAreaAndJobMarkers()
        } else if (polyPoints.size >= maxPolyPoints){
            listener.errorTooManyPoints()
        } else {
            listener.errorTooFewPoints()
        }
    }

    /**
     * Save service area and job markers
     */
    fun saveServiceAreaAndJobMarkers() {
        val editor = sharedPreferences.edit()
        editor.putString(SERVICE_AREA, gson.toJson(polyPoints))
        editor.putString(JOB_MARKERS, gson.toJson(savedJobMarkers))
        editor.commit()
    }

    /**
     * Load saved service area and job markers
     */
    fun loadSavedAreaAndMarkers(map: GoogleMap) {
        // TODO: fix serialization
//        val serviceAreaType: Type = object : TypeToken<MutableList<LatLng>>() {}.type
//        val jobMarkerType: Type = object : TypeToken<MutableList<LatLng>>() {}.type
//        val serviceArea: MutableList<LatLng> = gson.fromJson(sharedPreferences.getString(SERVICE_AREA, "") ?: "" , serviceAreaType)
//        val savedJobMarkers: MutableList<MarkerOptions> = gson.fromJson(sharedPreferences.getString(JOB_MARKERS, "") ?: "", jobMarkerType)
//
//        if (!serviceArea.equals("") && !savedJobMarkers.equals("")) {
//           val polygon = PolygonOptions()
//                .clickable(true)
//                .fillColor(fillColor)
//                .strokeWidth(0f)
//                .addAll(serviceArea)
//            loadPolygon(polygon, map)
//            resetMarkers(map, savedJobMarkers)
//        }
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
        return polygonPoints.containsLocation(pointToCheck, false)
    }

    /**
     * Retrieve all job markers inside polygon
     */
    fun getJobsInsidePolygon(): MutableList<MarkerOptions> {
        for (marker in jobMarkers) {
            if (polygonContainsPoint(polyPoints, marker.position)) {
                savedJobMarkers.add(marker)
                println("Found job marker to save at: " + marker.position.latitude + ", " + marker.position.longitude)
            } else {
                println("Job marker not found in polygon")
            }
        }
        return savedJobMarkers
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
        println("LatLngBounds NorthEast: " + latLngBounds.northeast + " SouthWest: " + latLngBounds.southwest + " Center: " + latLngBounds.center)
        return latLngBounds
    }

    /**
     * Add Random job markers
     */
    fun addRandomJobMarkers(map: GoogleMap, count: Int) {
        repeat(count) {
            var options = MarkerOptions()
                .position(generateRandomLatLngInBounds(getLatLngBounds(map)))
            addMarker(map, options.position, MarkerType.JOB)
            jobMarkers.add(options)
        }
    }

    /**
     * Reset markers provided by @param markers
     */
    fun resetMarkers(map: GoogleMap, markers: MutableList<MarkerOptions>) {
        for (markerOptions in markers) {
            map.addMarker(markerOptions)
        }
    }

    /**
     * Generate random LatLng within bounds
     */
    fun generateRandomLatLngInBounds(bounds: LatLngBounds): LatLng {
        var northEast = bounds.northeast
        var southWest = bounds.southwest

        return LatLng(Random.nextDouble(southWest.latitude, northEast.latitude), Random.nextDouble(southWest.longitude, northEast.longitude))
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
            addMarker(map, position, MarkerType.FENCEPOST)
            addLatLngToPolygon(position)
            addPolygonToMap(map)
        } else {
            listener.errorTooManyPoints()
        }
    }

    /**
     * Set shared preferences so we can persist our data
     */
    fun initSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }
}
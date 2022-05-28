package hollis.inman.polydemo.ui.main.map

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import hollis.inman.polydemo.ui.main.map.subunits.MapsListener

class MapViewModel: ViewModel() {

    /**
     * Map variables
     */
    lateinit var listener: MapsListener
    lateinit var polygon: Polygon
    var polyPoints: MutableList<LatLng> = mutableListOf()

    /**
     * Draw new polygon
     */

    /**
     * Edit polygon
     */

    /**
     * Save polygon
     */
    fun onSaveMapButtonClicked() {
        listener.onSaveMapButtonClicked()
    }

    /**
     * Load saved polygon
     */

    /**
     * Add LatLng point
     */
    fun addLatLng(latLng: LatLng) {
        polyPoints.add(latLng)
    }

    /**
     * Set new fence pin
     */
    fun onSetFencePost(map: GoogleMap, point: LatLng) {
        addLatLng(point)
        listener.onSetFencePost(map, point)
        if (polyPoints.count() < 2) {
            addLatLng(point)
            listener.errorTooFewPoints()
        } else {
            addLatLng(point)
            var polygon: PolygonOptions = PolygonOptions().clickable(true).addAll(polyPoints)
            map.addPolygon(
                polygon
            )
        }
    }
}
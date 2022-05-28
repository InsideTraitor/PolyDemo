package hollis.inman.polydemo.ui.main.map.subunits

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface MapsListener {
    fun onSaveMapButtonClicked()
    fun onSetFencePost(map: GoogleMap, position: LatLng)
    fun errorTooFewPoints()
}
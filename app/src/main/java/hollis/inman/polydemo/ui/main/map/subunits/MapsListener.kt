package hollis.inman.polydemo.ui.main.map.subunits

interface MapsListener {
    fun onSaveMapButtonClicked()
    fun onEditProfileNavigationClick()
    fun errorTooFewPoints()
    fun errorTooManyPoints()
}
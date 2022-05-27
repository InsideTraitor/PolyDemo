package hollis.inman.polydemo.ui.main.profile.view

import androidx.lifecycle.ViewModel
import hollis.inman.polydemo.ui.main.profile.view.subunits.ProfileListener

class ProfileViewModel() : ViewModel() {
    /**
     * Set variables
     */
    lateinit var listener: ProfileListener

    /**
     * Handle edit button click
     */
    fun onEditButtonClick() {
        listener.goToEditProfile()
    }

    /**
     * Handle save button click
     */


}
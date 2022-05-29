package hollis.inman.polydemo.ui.main.profile.edit

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import hollis.inman.polydemo.ui.main.profile.edit.subunits.EditProfileListener

class EditProfileViewModel2 : ViewModel() {
    lateinit var listener: EditProfileListener
    lateinit var sharedPreferences: SharedPreferences

    /**
     * Store user input
     */
    fun saveButtonClicked() {
        listener.saveButtonClicked()
    }

    /**
     * Proceed to map after service area button clicked
     */
    fun serviceAreaButtonClicked() {
        listener.serviceAreaButtonClicked()
    }

    /**
     * Set shared preferences so we can persist our data
     */
    fun initSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }
}
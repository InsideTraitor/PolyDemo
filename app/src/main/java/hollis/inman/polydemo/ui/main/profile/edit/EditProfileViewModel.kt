package hollis.inman.polydemo.ui.main.profile.edit

import androidx.lifecycle.ViewModel
import hollis.inman.polydemo.ui.main.profile.edit.subunits.EditProfileListener

class EditProfileViewModel2 : ViewModel() {
    lateinit var listener: EditProfileListener

    fun saveButtonClicked() {
        listener.saveButtonClicked()
    }
}
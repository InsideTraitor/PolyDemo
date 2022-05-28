package hollis.inman.polydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hollis.inman.polydemo.databinding.ActivityMainBinding
import hollis.inman.polydemo.ui.main.map.MapsFragment
import hollis.inman.polydemo.ui.main.map.subunits.MapsListener
import hollis.inman.polydemo.ui.main.profile.edit.EditProfileFragment
import hollis.inman.polydemo.ui.main.profile.edit.subunits.EditProfileListener
import hollis.inman.polydemo.ui.main.profile.view.ProfileFragment
import hollis.inman.polydemo.ui.main.profile.view.subunits.ProfileListener
import hollis.inman.polydemo.ui.main.utils.PermissionUtils
import hollis.inman.polydemo.ui.main.utils.PermissionsHelper

/**
 * Host Activity for app fragments
 */
class BaseActivity : AppCompatActivity(), ProfileListener, EditProfileListener, MapsListener {

    /**
     * Class variables
     */
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionsHelper: PermissionsHelper

    /**
     * OnCreate Activity Lifecycle call
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        requestMapPermission()
    }

    /**
     * Initialize class members
     */
    private fun init(savedInstanceState: Bundle?) {
        bindViews()
        showInitialFragment(savedInstanceState)
        permissionsHelper = PermissionsHelper(this, this)
    }

    /**
     * View Binding
     */
    private fun bindViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    /**
     * Display the initial fragment
     */
    private fun showInitialFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, ProfileFragment.newInstance())
                .commitNow()
        }
    }

    /**
     * Display Map Request Fine/Coarse Permission to User
     */
    fun requestMapPermission() {
        permissionsHelper.getMapsPermission()
    }

    override fun goToEditProfile() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, EditProfileFragment.newInstance())
            .commitNow()
    }

    override fun saveButtonClicked() {
        //TODO: Add save logic

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, ProfileFragment.newInstance())
            .commitNow()
    }

    override fun serviceAreaButtonClicked() {
//        PermissionUtils.requestLocationPermissions(this, )
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, MapsFragment.newInstance())
            .commitNow()
    }

    override fun onSaveMapButtonClicked() {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, EditProfileFragment.newInstance())
            .commitNow()
    }

    override fun onSetFencePost(map: GoogleMap, position: LatLng) {
        Toast.makeText(this, position.latitude.toString() + ", " + position.longitude.toString(), Toast.LENGTH_SHORT).show()
        map.addMarker(MarkerOptions().position(position).title(position.latitude.toString() + ", " +position.longitude.toString()))
    }

    override fun errorTooFewPoints() {
        Toast.makeText(this, "Too few points", Toast.LENGTH_SHORT).show()
    }
}
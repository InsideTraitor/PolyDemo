package hollis.inman.polydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hollis.inman.polydemo.databinding.ActivityMainBinding
import hollis.inman.polydemo.ui.main.MainFragment
import hollis.inman.polydemo.ui.main.utils.PermissionsHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionsHelper: PermissionsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
        requestMapPermission()
    }

    fun requestMapPermission() {
        permissionsHelper = PermissionsHelper(this, this)
        permissionsHelper.getMapsPermission()
    }
}
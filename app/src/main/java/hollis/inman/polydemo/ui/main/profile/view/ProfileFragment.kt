package hollis.inman.polydemo.ui.main.profile.view

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hollis.inman.polydemo.BaseActivity
import hollis.inman.polydemo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        bindViews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.listener = context as BaseActivity
        init()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loadSharedPreferences()
    }

    private fun init() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        loadSharedPreferences()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnEdit.setOnClickListener { viewModel.onEditButtonClick() }
    }

    private fun bindViews() {
        binding = FragmentProfileBinding.inflate(layoutInflater)
    }

    fun loadSharedPreferences() {
        binding.txtFirstName.setText(sharedPreferences.getString(BaseActivity.FIRST_NAME, "first name"))
        binding.txtLastName.setText(sharedPreferences.getString(BaseActivity.LAST_NAME, "last name"))
        binding.txtPrimaryPhone.setText(sharedPreferences.getString(BaseActivity.PRIMARY_PHONE, "primary phone"))
        binding.txtEmail.setText(sharedPreferences.getString(BaseActivity.EMAIL, "email"))
    }
}
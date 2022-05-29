package hollis.inman.polydemo.ui.main.profile.edit

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hollis.inman.polydemo.BaseActivity
import hollis.inman.polydemo.BaseActivity.Companion.EMAIL
import hollis.inman.polydemo.BaseActivity.Companion.FIRST_NAME
import hollis.inman.polydemo.BaseActivity.Companion.LAST_NAME
import hollis.inman.polydemo.BaseActivity.Companion.MIDDLE_NAME
import hollis.inman.polydemo.BaseActivity.Companion.PRIMARY_PHONE
import hollis.inman.polydemo.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel2
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindViews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel2::class.java)
        viewModel.listener = context as BaseActivity
        init()
    }

    fun init() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        setClickListeners()
        viewModel.initSharedPreferences(sharedPreferences)
        loadSharedPreferences()
    }

    fun setClickListeners() {
        binding.btnSave.setOnClickListener {
            viewModel.saveButtonClicked()
            saveSharedPreferences()
        }
        binding.btnServiceArea.setOnClickListener { viewModel.serviceAreaButtonClicked() }
    }

    fun bindViews() {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
    }

    fun loadSharedPreferences() {
        binding.etxtFirstName.setText(sharedPreferences.getString(FIRST_NAME, "first name"))
        binding.etxtMiddleName.setText(sharedPreferences.getString(MIDDLE_NAME, "middle name"))
        binding.etxtLastName.setText(sharedPreferences.getString(LAST_NAME, "last name"))
        binding.etxtPrimaryPhone.setText(sharedPreferences.getString(PRIMARY_PHONE, "primary phone"))
        binding.etxtEmail.setText(sharedPreferences.getString(EMAIL, "email"))
    }

    fun saveSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putString(FIRST_NAME, binding.etxtFirstName.text.toString())
        editor.putString(MIDDLE_NAME, binding.etxtMiddleName.text.toString())
        editor.putString(LAST_NAME, binding.etxtLastName.text.toString())
        editor.putString(PRIMARY_PHONE, binding.etxtPrimaryPhone.text.toString())
        editor.putString(EMAIL, binding.etxtEmail.text.toString())
        editor.commit()
    }

}
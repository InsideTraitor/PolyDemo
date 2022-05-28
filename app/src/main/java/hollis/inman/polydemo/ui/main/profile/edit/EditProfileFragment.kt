package hollis.inman.polydemo.ui.main.profile.edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hollis.inman.polydemo.BaseActivity
import hollis.inman.polydemo.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel2

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
        setClickListeners()
    }

    fun setClickListeners() {
        binding.btnSave.setOnClickListener { viewModel.saveButtonClicked() }
        binding.btnServiceArea.setOnClickListener { viewModel.serviceAreaButtonClicked() }
    }

    fun bindViews() {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
    }

}
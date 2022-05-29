package hollis.inman.polydemo.ui.main.profile.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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

    private fun init() {
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnEdit.setOnClickListener { viewModel.onEditButtonClick() }
    }

    private fun bindViews() {
        binding = FragmentProfileBinding.inflate(layoutInflater)
    }
}
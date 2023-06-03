package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentMemberInviteResultBinding
import kids.baba.mobile.presentation.event.InviteResultEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.activity.IntroActivity
import kids.baba.mobile.presentation.viewmodel.InviteMemberResultViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InviteMemberResultFragment : Fragment() {

    private var _binding: FragmentMemberInviteResultBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InviteMemberResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLogin()
        collectEvent()
        bindViewModel()
        binding.topAppBar.ivBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnComplete.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is InviteResultEvent.SuccessGetInvitationInfo -> {
                        binding.groupView.tvDesc.text = it.data.relationGroup
                        binding.relationView.tvDesc.text = it.data.relationName
                    }
                    is InviteResultEvent.BackButtonClicked -> {

                    }
                    is InviteResultEvent.GoToMyPage -> {

                    }
                    is InviteResultEvent.ShowSnackBar -> {

                    }
                    else -> {}
                }
            }
        }
    }

    private fun checkLogin() {
        lifecycleScope.launch {
            if (viewModel.checkLogin()) {
                viewModel.registerMember()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_request),
                    Toast.LENGTH_LONG
                )
                    .show()
                IntroActivity.startActivity(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberInviteResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

}
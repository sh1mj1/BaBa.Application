package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAddGroupBinding
import kids.baba.mobile.presentation.viewmodel.AddGroupViewModel
import kids.baba.mobile.presentation.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: AddGroupViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.ivBackButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.btnAdd.setOnClickListener {
            lifecycleScope.launch {
                val name = binding.nameView.etInput.text.toString()
                viewModel.addGroup(name, "#FFAEBA").join()
                myPageViewModel.loadGroups()
                requireActivity().finish()
            }
        }
    }

}
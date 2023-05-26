package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditBabyProfileBinding
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.viewmodel.BabyEditProfileBottomSheetViewModel

@AndroidEntryPoint
class BabyEditProfileBottomSheet(val itemClick: (String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditBabyProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: BabyEditProfileBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameView.tvEditButton.setOnClickListener {
            val name = binding.nameView.tvEdit.text.toString()
            itemClick(name)
            viewModel.edit(babyId = viewModel.baby.value?.memberId ?: "", name = name)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditBabyProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BabyEditProfileBottomSheet"
        const val SELECTED_BABY_KEY = "SELECTED_BABY"
    }
}
package kids.baba.mobile.presentation.view.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.databinding.FragmentTextInputBabyInfoBinding
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserChatType
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.viewmodel.InputBabiesInfoViewModel

class TextInputBabyInfoFragment : Fragment() {
    private var _binding: FragmentTextInputBabyInfoBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val inputBabiesInfoViewModel: InputBabiesInfoViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextInputBabyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        focusEditText()

        binding.apply {
            etTextInput.setOnEditorActionListener(getEditorActionListener(tvSend))

            tvSend.setOnClickListener {
                when(val uiState = inputBabiesInfoViewModel.uiState.value) {
                    is InputBabiesInfoUiState.InputBabyName -> {
                        val babyName = etTextInput.text.toString()
                        inputBabiesInfoViewModel.setBabyName(
                            ChatItem.UserChatItem(
                                etTextInput.text.toString(),
                                UserChatType.BABY_NAME,
                                canModify = true,
                                isModifying = false
                            ),
                            babyName
                        )
                    }
                    is InputBabiesInfoUiState.ModifyName -> {
                        val babyName = etTextInput.text.toString()
                        inputBabiesInfoViewModel.modifyBabyName(
                            ChatItem.UserChatItem(
                                etTextInput.text.toString(),
                                UserChatType.BABY_NAME,
                                canModify = true,
                                isModifying = false
                            ),
                            babyName,
                            uiState.position
                        )
                    }
                    else -> {

                    }
                }
                etTextInput.setText("")
                hideKeyboard()
            }
        }
    }

    private fun focusEditText() {
        binding.etTextInput.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etTextInput, 0)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etTextInput.windowToken, 0)
        binding.etTextInput.clearFocus()
    }

    private fun getEditorActionListener(view: View): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.callOnClick()
            }
            false
        }
    }

}

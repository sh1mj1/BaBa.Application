package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.state.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageGroupUseCase: GetMyPageGroupUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {
    val myName = MutableStateFlow("손제인")
    val myStatusMessage = MutableStateFlow("상테메시지 설정해요~")
    val groupAddButton = MutableStateFlow("+ 그룹만들기")


    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Idle)
    val uiState = _uiState

    fun loadGroups() = viewModelScope.launch {
        getMyPageGroupUseCase.get().catch {

        }.collect {
            _uiState.value = MyPageUiState.LoadMember(it.groups)
        }
    }

    fun loadBabies() = viewModelScope.launch {
        getBabiesUseCase.getBabies().catch { }.collect {
            _uiState.value = MyPageUiState.LoadBabies(it.myBaby + it.others)
        }
    }

    fun getMyInfo() = viewModelScope.launch {
        val member = getMemberUseCase.getMe()
        Log.e("me", "$member")
        myName.value = member.name
        myStatusMessage.value = member.introduction
    }
}
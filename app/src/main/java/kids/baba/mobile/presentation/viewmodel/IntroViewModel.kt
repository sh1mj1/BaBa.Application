package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.model.UserProfile
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<IntroEvent>()
    val eventFlow = _eventFlow.asEventFlow()


    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            getMemberUseCase.getMe()
                .catch {
                    if(it is TokenEmptyException){
                        _eventFlow.emit(IntroEvent.StartOnBoarding)
                    } else {
                        _eventFlow.emit(IntroEvent.IntroError)
                    }
                }
                .collect {
                    _eventFlow.emit(IntroEvent.MoveToMain(it))
                }
        }
    }

    fun isOnBoardingEnd() {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToLogin)
        }
    }

    fun isLoginSuccess(member: MemberModel) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToMain(member))
        }
    }

    fun isSignUpStart(signToken: String) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToCreateUserProfile(signToken))
        }
    }

    fun isNeedToBabiesInfo(signToken: String, userProfile: UserProfile) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToInputBabiesInfo(signToken, userProfile))
        }
    }

    fun isNeedToAgree(socialToken: String) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToAgree(socialToken))
        }
    }

    fun isSignUpSuccess(member: MemberModel) {

    }
}
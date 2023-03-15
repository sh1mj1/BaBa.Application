package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.TokenResponse
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getMe(accessToken: String): Flow<MemberModel>

    fun signUpWithBabiesInfo(signToken: String, signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo): Flow<TokenResponse>

    fun signUpWithInviteCode(signToken: String, signUpRequestWithInviteCode: SignUpRequestWithInviteCode): Flow<TokenResponse>
}

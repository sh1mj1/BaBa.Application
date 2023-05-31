package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMyPageBinding
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.view.fragment.AddBabyFragmentDirections
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.ADD_BABY_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.ADD_GROUP_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INTENT_PAGE_NAME
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_CODE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_RESULT_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_WITH_CODE_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.SETTING_PAGE
import kids.baba.mobile.presentation.viewmodel.MyPageActivityViewModel

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph
    private lateinit var binding: ActivityMyPageBinding
    private val viewModel: MyPageActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        instance = this

        collectEvent()
        setStartDestination()
    }

    private fun setStartDestination() {
        when (intent.getStringExtra(INTENT_PAGE_NAME)) {
            ADD_BABY_PAGE -> setNavStart(R.id.add_baby_fragment)
            INVITE_WITH_CODE_PAGE -> setNavStart(R.id.input_invite_fragment)
            INVITE_MEMBER_PAGE -> setNavStart(R.id.invite_member_fragment)
            INVITE_MEMBER_RESULT_PAGE -> setNavStartWithCode(R.id.invite_member_result_fragment, INVITE_CODE)
            SETTING_PAGE -> setNavStart(R.id.setting_fragment)
            ADD_GROUP_PAGE -> setNavStart(R.id.add_group_fragment)
            BABY_DETAIL_PAGE -> setNavStartWithArg(R.id.baby_detail_fragment, BABY_DETAIL_INFO)
        }
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    MyPageEvent.CompleteAddBaby -> {
                        val action =
                            AddBabyFragmentDirections.actionAddBabyFragmentToAddCompleteFragment()
                        navController.navigate(action)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController.navInflater.inflate(R.navigation.my_page_nav_graph)
    }

    private fun setNavStart(fragment: Int) {
        navGraph.setStartDestination(fragment)
        navController.graph = navGraph
    }

    private fun setNavStartWithArg(fragment: Int, argumentName: String) {
        val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(argumentName, MemberUiModel::class.java)
        } else {
            intent.getParcelableExtra(argumentName)
        }

        key?.let {
            val bundle = bundleOf(argumentName to it)
            navGraph.setStartDestination(fragment)
            navController.setGraph(navGraph, bundle)
        }
    }

    private fun setNavStartWithCode(fragment: Int, argumentName: String) {
        val key = intent.getStringExtra(INVITE_CODE)
        key?.let {
            val bundle = bundleOf(argumentName to it)
            navGraph.setStartDestination(fragment)
            navController.setGraph(navGraph, bundle)
        }
    }



    companion object {
        lateinit var instance: MyPageActivity
        fun startActivity(context: Context, pageName: String) {
            val intent = Intent(context, MyPageActivity::class.java).apply {
                putExtra(INTENT_PAGE_NAME, pageName)
            }
            context.startActivity(intent)
        }

        fun startActivityWithMember(
            context: Context,
            pageName: String,
            argumentName: String,
            memberUiModel: MemberUiModel
        ) {
            val intent = Intent(context, MyPageActivity::class.java).apply {
                putExtra(INTENT_PAGE_NAME, pageName)
                putExtra(argumentName, memberUiModel)
            }
            context.startActivity(intent)

        }

        fun startActivityWithCode(
            context: Context,
            pageName: String,
            argumentName: String,
            inviteCode: String
        ) {
            val intent = Intent(context, MyPageActivity::class.java).apply {
                putExtra(INTENT_PAGE_NAME, pageName)
                putExtra(argumentName, inviteCode)
            }
            context.startActivity(intent)
        }

    }
}

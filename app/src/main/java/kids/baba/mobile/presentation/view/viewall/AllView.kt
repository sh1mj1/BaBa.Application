package kids.baba.mobile.presentation.view.viewall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAllViewBinding
import kids.baba.mobile.presentation.adapter.AllViewAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.viewall.GatheringViewViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AllView @Inject constructor() : Fragment() {

    private var _binding: FragmentAllViewBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: GatheringViewViewModel by viewModels()
    private lateinit var adapter: AllViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = AllViewAdapter()
        binding.rvAllBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvAllBabies.layoutManager = manager

        viewLifecycleOwner.repeatOnStarted {
            viewModel.allAlbumListState.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
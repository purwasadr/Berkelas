package com.alurwa.berkelas.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.MainCardAdapter
import com.alurwa.berkelas.adapter.MainMenuAdapter
import com.alurwa.berkelas.databinding.FragmentHomeBinding
import com.alurwa.berkelas.extension.EXTRA_MODE
import com.alurwa.berkelas.extension.MODE_ADD
import com.alurwa.berkelas.model.CardHomeItem
import com.alurwa.berkelas.model.MainMenuItem
import com.alurwa.berkelas.ui.attendance.AttendanceActivity
import com.alurwa.berkelas.ui.cashall.CashAllActivity
import com.alurwa.berkelas.ui.homecardaddedit.HomeCardAddEditActivity
import com.alurwa.berkelas.ui.main.MainViewModel
import com.alurwa.berkelas.ui.picket.PicketActivity
import com.alurwa.berkelas.ui.subject.SubjectActivity
import com.alurwa.berkelas.util.CardColor
import com.alurwa.berkelas.util.SnackbarUtil
import com.alurwa.common.model.HomeCard
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val mainMenuAdapter: MainMenuAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainMenuAdapter {
            if (isCanSelectMenu) {
                mainMenuNavigateTo(it)
            } else {
                SnackbarUtil.showShort(binding.root, "Anda belum masuk room")
            }
        }
    }

    private val mainHeaderCardAdapter: MainCardAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainCardAdapter(
            onContentClick = {

            },
            onAddClick = {
                navigateToAddHomeCard()
            }
        )
    }

    private var isCanSelectMenu = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListMenu()
        setupHeaderCard()
        fillMenuMainAdapter()
        observeHomeCard()
        observeUser()
    }

    private fun setupListMenu() {
        with(binding) {
            listMenu.setHasFixedSize(true)
            listMenu.layoutManager = GridLayoutManager(requireContext(), 4)
            listMenu.adapter = mainMenuAdapter
        }
    }

    private fun setupHeaderCard() {
        with(binding) {
            // listCard.setHasFixedSize(true)
            listCard.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            listCard.adapter = mainHeaderCardAdapter

            PagerSnapHelper().attachToRecyclerView(listCard)
        }
    }

    private fun observeUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.filterNotNull().collectLatest {
                    isCanSelectMenu = it.roomId.isNotEmpty()
                }
            }
        }
    }

    private fun observeHomeCard() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeHomeCard.collectLatest { result ->
                    result.onSuccess {
                        fillMainHeaderCard(it)
                    }
                }
            }
        }
    }

    private fun navigateToAddHomeCard() {
        Intent(requireContext(), HomeCardAddEditActivity::class.java)
            .putExtra(EXTRA_MODE, MODE_ADD)
            .also {
                startActivity(it)
            }
    }

    private fun fillMenuMainAdapter() {
        val menuMains = listOf(
            MainMenuItem(
                name = getString(R.string.tv_subject),
                drawableRes = R.drawable.ic_round_subject_24
            ),

            MainMenuItem(
                name = getString(R.string.tv_cash),
                drawableRes = R.drawable.ic_round_attach_money_24
            ),
            MainMenuItem(
                name = getString(R.string.tv_picket),
                drawableRes = R.drawable.ic_round_cleaning_services_24
            ),
            MainMenuItem(
                name = getString(R.string.tv_presence),
                drawableRes = R.drawable.ic_round_ballot_24
            )
        )

        mainMenuAdapter.submitList(
            menuMains
        )
    }

    private fun fillMainHeaderCard(items: List<HomeCard>) {
        if (items.isEmpty()) {
            listOf(
                CardHomeItem.Content(
                    id = "wme",
                    caption = "Coba",
                    backgroundColor = CardColor.PURPLE.code
                ),
                CardHomeItem.Add
            ).also {
                mainHeaderCardAdapter.submitList(it)
            }
        } else {
            val itemCheck = if (items.size > 4) {
                items.take(4)
            } else {
                items
            }

            val transform = List(itemCheck.size) {
                val item = itemCheck[it]

                if (it < itemCheck.size) {
                    CardHomeItem.Content(
                        id = item.id,
                        caption = item.text,
                        backgroundColor = item.backgroundColor
                    )
                } else {
                    CardHomeItem.Add
                }
            }

            mainHeaderCardAdapter.submitList(transform)
        }
    }

    private fun mainMenuNavigateTo(position: Int) {
        when (position) {
            0 -> {
                Intent(requireContext(), SubjectActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
            1 -> {
                Intent(requireContext(), CashAllActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
            2 -> {

                Intent(requireContext(), PicketActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
            3 -> {
                Intent(requireContext(), AttendanceActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
        }
    }
}
package com.alurwa.berkelas.ui.account

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.adapter.AccountMenuAdapter
import com.alurwa.berkelas.databinding.FragmentAccountBinding
import com.alurwa.berkelas.model.MainMenuItem
import com.alurwa.berkelas.ui.accountedit.AccountEditActivity
import com.alurwa.berkelas.ui.main.MainViewModel
import com.alurwa.berkelas.ui.roomlist.RoomListActivity
import com.alurwa.common.model.User
import com.alurwa.common.model.UserWithoutRoom
import com.alurwa.common.model.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val accountMenuAdapter: AccountMenuAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AccountMenuAdapter {
            navigateTo(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAccountMenu()

        fillAccountMenuAdapter()

        observeMyUser()
    }

    private fun observeMyUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUser.collectLatest { result ->
                    result.onSuccess {
                        val data = it
                        if (data != null) {
                            fillUserData(data)

                            viewModel.setUser(
                                data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun mapUserToUserWithoutSchool(user: User) =
        with(user) {
            UserWithoutRoom(
                uid = uid,
                email = email,
                profileImgUrl = profileImgUrl,
                username = username,
                fullName = fullName,
                nickname = nickname,
                dateOfBirth = dateOfBirth,
                gender = gender
            )
        }

    private fun setupAccountMenu() {
        with(binding) {
            listMenuAccount.setHasFixedSize(true)
            listMenuAccount.layoutManager = LinearLayoutManager(requireContext())
            listMenuAccount.adapter = accountMenuAdapter
        }
    }

    private fun fillUserData(user: User) {
        with(binding) {
            txtEmail.text = user.email
            txtUsername.text = user.username
        }
    }

    private fun fillAccountMenuAdapter() {
        val accountMenuList = listOf(
            MainMenuItem(
                "Edit Akun",
                true
            ),
            MainMenuItem(
                "Ganti Room",
                true
            ),
            MainMenuItem(
                "Setting",
                true
            )
        )

        accountMenuAdapter.submitList(accountMenuList)
    }

    private fun navigateTo(position: Int) {
        when (position) {
            0 -> {
                val user = viewModel.user.value
                val userWS = mapUserToUserWithoutSchool(user ?: User.EMPTY)

                Intent(requireContext(), AccountEditActivity::class.java)
                    .putExtra(AccountEditActivity.EXTRA_USER_WITHOUT_SCHOOL, userWS)
                    .also {
                        startActivity(it)
                    }
            }

            1 -> {
                val roomId = viewModel.user.value?.roomId ?: ""

                Intent(requireContext(), RoomListActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
        }
    }
}
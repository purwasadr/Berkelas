package com.alurwa.berkelas.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alurwa.berkelas.R
import com.alurwa.berkelas.adapter.AccountMenuAdapter
import com.alurwa.berkelas.databinding.FragmentAccountBinding
import com.alurwa.berkelas.model.AccountMenuItem
import com.alurwa.berkelas.ui.accountedit.AccountEditActivity
import com.alurwa.berkelas.ui.main.MainViewModel
import com.alurwa.berkelas.ui.roomlist.RoomListActivity
import com.alurwa.berkelas.ui.roomuserlist.RoomUserListActivity
import com.alurwa.common.model.User
import com.alurwa.common.model.UserWithoutRoom
import dagger.hilt.android.AndroidEntryPoint

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        fillAccountMenuAdapter()
    }

    private fun mapUserToUserWithoutRoom(user: User) =
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

    private fun fillAccountMenuAdapter() {
        val accountMenuList = listOf(
            AccountMenuItem(
                "Edit Akun",
                R.drawable.ic_round_person_24,
                true
            ),
            AccountMenuItem(
                "Ganti Room",
                R.drawable.ic_round_meeting_room_24,
                true
            ),
            AccountMenuItem(
                "Daftar Anggota",
                R.drawable.ic_round_people_alt_24,
                true
            ),
            AccountMenuItem(
                "Setting",
                R.drawable.ic_round_people_alt_24,
                true
            )
        )

        accountMenuAdapter.submitList(accountMenuList)
    }

    private fun navigateTo(position: Int) {
        when (position) {
            0 -> {
                val user = viewModel.user.value
                val userWS = mapUserToUserWithoutRoom(user ?: User.EMPTY)

                Intent(requireContext(), AccountEditActivity::class.java)
                    .putExtra(AccountEditActivity.EXTRA_USER_WITHOUT_SCHOOL, userWS)
                    .also {
                        startActivity(it)
                    }
            }

            1 -> {

                Intent(requireContext(), RoomListActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
            2 -> {
                Intent(requireContext(), RoomUserListActivity::class.java)
                    .also {
                        startActivity(it)
                    }
            }
        }
    }
}
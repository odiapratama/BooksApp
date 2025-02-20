package com.problemsolver.event.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.problemsolver.event.R
import com.problemsolver.event.Settings
import com.problemsolver.event.UpcomingEvent
import com.problemsolver.event.data.model.AllEvent
import com.problemsolver.event.data.model.Status
import com.problemsolver.event.data.source.local.dataStore
import com.problemsolver.event.databinding.FragmentHomeBinding
import com.problemsolver.event.sync.ReminderScheduler
import com.problemsolver.event.utils.changeDrawable
import com.problemsolver.event.utils.collectLatestStarted
import com.problemsolver.event.utils.emptyUpcomingEvent
import com.problemsolver.event.utils.gone
import com.problemsolver.event.utils.isDarkModeEnabled
import com.problemsolver.event.utils.launchStarted
import com.problemsolver.event.utils.onQueryTextChanged
import com.problemsolver.event.utils.toDataBuilder
import com.problemsolver.event.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: EventViewModel by viewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var finishedAdapter: EventAdapter
    private var reminderEvent: UpcomingEvent? = null
    private var settings: Settings? = null

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData("")
        initView()
    }

    private fun getData(search: String) {
        collectLatestStarted(viewModel.getAllEvents(search)) {
            when (it.status) {
                Status.LOADING -> {
                    binding.rvUpcoming.veil()
                    binding.rvFinished.veil()
                    binding.tvUpcoming.visible()
                    binding.tvFinished.visible()
                }

                Status.SUCCESS -> {
                    upcomingAdapter.setData(it.data?.upcoming?.take(5) ?: emptyList())
                    finishedAdapter.setData(it.data?.finished?.take(5) ?: emptyList())
                    handleEventView(it.data)
                    saveUpcomingReminder(it.data)
                    binding.rvUpcoming.unVeil()
                    binding.rvFinished.unVeil()
                }

                Status.ERROR -> {
                    binding.rvUpcoming.unVeil()
                    binding.rvFinished.unVeil()
                    binding.tvUpcoming.gone()
                    binding.tvFinished.gone()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAppSettings() {
        launchStarted {
            viewModel.getAppPreferences()
                .collectLatest {
                    settings = it
                }
        }
    }

    private fun setUpButtonReminder() {
        if (settings?.upcomingEvent?.name.isNullOrEmpty()) {
            binding.btnNotification.changeDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_notification_off
                )
            )
        } else {
            binding.btnNotification.changeDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_notification_on
                )
            )
        }
    }

    private fun handleEventView(data: AllEvent?) {
        with(binding) {
            if (data?.upcoming.isNullOrEmpty()) tvUpcoming.gone()
            else tvUpcoming.visible()
            if (data?.finished.isNullOrEmpty()) tvFinished.gone()
            else tvFinished.visible()
            if (data?.upcoming.isNullOrEmpty() && data?.finished.isNullOrEmpty()) {
                tvEmpty.visible()
            } else {
                tvEmpty.gone()
            }
        }
    }

    private fun saveUpcomingReminder(data: AllEvent?) {
        reminderEvent = data?.toDataBuilder()
        launchStarted {
            requireContext().dataStore.updateData {
                it.toBuilder().setUpcomingEvent(reminderEvent).build()
            }
        }
        getAppSettings()
        setUpButtonReminder()
    }

    private fun initView() {
        if (requireContext().isDarkModeEnabled()) {
            binding.btnThemeMode.changeDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_light_mode
                )
            )
        } else {
            binding.btnThemeMode.changeDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_night_mode
                )
            )
        }
        upcomingAdapter = EventAdapter {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        finishedAdapter = EventAdapter {
            val intent = Intent(requireContext(), EventDetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
        with(binding) {
            rvUpcoming.setAdapter(upcomingAdapter)
            rvUpcoming.setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            rvUpcoming.addVeiledItems(3)

            rvFinished.setAdapter(finishedAdapter)
            rvFinished.setLayoutManager(LinearLayoutManager(requireContext()))
            rvFinished.addVeiledItems(3)

            searchView.onQueryTextChanged(viewLifecycleOwner.lifecycleScope) {
                getData(it)
            }

            btnThemeMode.setOnClickListener {
                val themeMode =
                    if (settings?.themeMode == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO
                    else AppCompatDelegate.MODE_NIGHT_YES
                launchStarted {
                    requireContext().dataStore.updateData {
                        it.toBuilder().setThemeMode(themeMode).build()
                    }
                }
            }

            btnNotification.setOnClickListener {
                if (settings?.upcomingEvent?.name.isNullOrEmpty()) {
                    launchStarted {
                        requireContext().dataStore.updateData {
                            it.toBuilder().setUpcomingEvent(reminderEvent).build()
                        }
                    }
                    btnNotification.changeDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_notification_on
                        )
                    )
                    reminderScheduler.scheduleReminder()
                    Toast.makeText(
                        requireContext(),
                        "Reminder set for upcoming event",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    launchStarted {
                        requireContext().dataStore.updateData {
                            it.toBuilder().setUpcomingEvent(emptyUpcomingEvent()).build()
                        }
                    }
                    btnNotification.changeDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_notification_off
                        )
                    )
                    reminderScheduler.cancelReminder()
                    Toast.makeText(
                        requireContext(),
                        "Reminder canceled for upcoming event",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                getAppSettings()
            }
        }
    }
}
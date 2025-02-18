package com.problemsolver.event.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.problemsolver.event.R
import com.problemsolver.event.data.model.Event
import com.problemsolver.event.data.model.Status
import com.problemsolver.event.databinding.ActivityEventDetailBinding
import com.problemsolver.event.utils.disable
import com.problemsolver.event.utils.enable
import com.problemsolver.event.utils.formatDateTime
import com.problemsolver.event.utils.gone
import com.problemsolver.event.utils.setTextHtml
import com.problemsolver.event.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity(R.layout.activity_event_detail) {

    private val binding by viewBinding(ActivityEventDetailBinding::bind)
    private val viewModel: EventViewModel by viewModels()
    private var event: Event? = null
    private var favorite: Event? = null
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        id = intent.getIntExtra("id", 0)
        initObserver()
        initData()
        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_share -> {
                event?.description?.let { shareContent(it) }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        viewModel.getEventDetail(id)
        lifecycleScope.launch {
            favorite = viewModel.getFavoriteEventById(id)
        }
    }

    private fun initObserver() {
        viewModel.eventDetail.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.veilLayout.veil()
                    binding.btnJoin.gone()
                    binding.btnJoin.disable()
                }

                Status.SUCCESS -> {
                    binding.veilLayout.unVeil()
                    it.data?.let { eventDetail ->
                        event = eventDetail
                        with(binding) {
                            ivCover.load(eventDetail.mediaCover) {
                                crossfade(true)
                            }
                            tvTitle.text = eventDetail.name
                            tvOwner.text = eventDetail.ownerName
                            tvLocation.text = eventDetail.cityName
                            tvQuota.text = String.format(
                                Locale.getDefault(), "Quota: %d",
                                (eventDetail.quota - eventDetail.registrants)
                            )
                            tvTime.text = formatDateTime(eventDetail.beginTime)
                            tvDescription.setTextHtml(eventDetail.description)
                            btnJoin.visible()
                            btnJoin.enable()
                            if (favorite != null)
                                btnFavorite.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this@EventDetailActivity,
                                        R.drawable.ic_red_heart
                                    )
                                )
                            else btnFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@EventDetailActivity,
                                    R.drawable.ic_grey_heart
                                )
                            )
                        }
                    }
                }

                Status.ERROR -> {
                    binding.btnJoin.gone()
                    binding.veilLayout.unVeil()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initView() {
        binding.btnJoin.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event?.link))
            startActivity(browserIntent)
        }
        binding.btnFavorite.setOnClickListener {
            if (favorite != null) {
                event?.let { viewModel.deleteFavoriteEvent(it.id) }
                binding.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@EventDetailActivity,
                        R.drawable.ic_grey_heart
                    )
                )
                Toast.makeText(
                    this@EventDetailActivity,
                    "Event removed from favorite",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                event?.let {
                    viewModel.insertFavoriteEvent(it)
                }
                binding.btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@EventDetailActivity,
                        R.drawable.ic_red_heart
                    )
                )
                Toast.makeText(
                    this@EventDetailActivity,
                    "Event added to favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareContent(content: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)

        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
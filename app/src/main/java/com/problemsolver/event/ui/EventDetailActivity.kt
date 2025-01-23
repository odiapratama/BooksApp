package com.problemsolver.event.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.problemsolver.event.R
import com.problemsolver.event.data.model.Status
import com.problemsolver.event.databinding.ActivityEventDetailBinding
import com.problemsolver.event.utils.disable
import com.problemsolver.event.utils.enable
import com.problemsolver.event.utils.formatDateTime
import com.problemsolver.event.utils.gone
import com.problemsolver.event.utils.setTextHtml
import com.problemsolver.event.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity(R.layout.activity_event_detail) {

    private val binding by viewBinding(ActivityEventDetailBinding::bind)
    private val viewModel: EventViewModel by viewModels()
    private var sharedContent = ""
    private var link =  ""
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
                shareContent(sharedContent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initData() {
        viewModel.getEventDetail(id)
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
                        sharedContent = eventDetail.description
                        link = eventDetail.link
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
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
    }

    private fun shareContent(content: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)

        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
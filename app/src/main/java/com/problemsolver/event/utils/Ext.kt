package com.problemsolver.event.utils

import android.content.Context
import android.content.res.Configuration
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.problemsolver.event.UpcomingEvent
import com.problemsolver.event.data.model.AllEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> CoroutineScope.safeApiCall(
    onError: (Exception) -> Unit = { },
    execute: suspend () -> T
) {
    launch {
        try {
            execute()
        } catch (e: Exception) {
            onError(e)
        }
    }
}

fun <T> LifecycleOwner.collectLatestStarted(
    flow: Flow<T>,
    block: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                block(it)
            }
        }
    }
}

fun LifecycleOwner.launchStarted(
    block: suspend () -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun TextView.setTextHtml(text: String) {
    this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Button.disable() {
    this.isEnabled = false
}

fun Button.enable() {
    this.isEnabled = true
}

fun SearchView.onQueryTextChanged(
    lifecycleScope: LifecycleCoroutineScope,
    debounceTime: Long = 1500L,
    onQueryTextChanged: (String) -> Unit
) {
    var searchJob: Job? = null

    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { onQueryTextChanged(it) }
            this@onQueryTextChanged.clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                newText?.let { query ->
                    delay(debounceTime)
                    onQueryTextChanged(query)
                }
            }
            return true
        }
    })
}

fun Context.isDarkModeEnabled(): Boolean {
    return (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

fun AllEvent?.toDataBuilder(): UpcomingEvent {
    val data = if (!this?.upcoming.isNullOrEmpty()) this?.upcoming?.firstOrNull()
    else this?.finished?.firstOrNull()
    return UpcomingEvent.newBuilder()
        .setId(data?.id ?: 0)
        .setName(data?.name ?: "")
        .setSummary(data?.summary ?: "")
        .setDescription(data?.description ?: "")
        .setImageLogo(data?.imageLogo ?: "")
        .setMediaCover(data?.mediaCover ?: "")
        .setCategory(data?.category ?: "")
        .setOwnerName(data?.ownerName ?: "")
        .setCityName(data?.cityName ?: "")
        .setQuota(data?.quota ?: 0)
        .setRegistrants(data?.registrants ?: 0)
        .setBeginTime(data?.beginTime ?: "")
        .setEndTime(data?.endTime ?: "")
        .setLink(data?.link ?: "")
        .build()
}

fun emptyUpcomingEvent(): UpcomingEvent {
    return UpcomingEvent.newBuilder()
        .setId(0)
        .setName("")
        .setSummary("")
        .setDescription("")
        .setImageLogo("")
        .setMediaCover("")
        .setCategory("")
        .setOwnerName("")
        .setCityName("")
        .setQuota(0)
        .setRegistrants(0)
        .setBeginTime("")
        .setEndTime("")
        .setLink("")
        .build()
}
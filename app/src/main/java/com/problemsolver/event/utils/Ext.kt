package com.problemsolver.event.utils

import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
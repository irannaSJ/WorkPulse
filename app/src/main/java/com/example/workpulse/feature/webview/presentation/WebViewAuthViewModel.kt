package com.example.workpulse.feature.webview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.core.datastore.CookieStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewAuthViewModel @Inject constructor(
    private val cookieStorage: CookieStorage
) : ViewModel() {

    private val _sid = MutableStateFlow<String?>(null)
    val sid: StateFlow<String?> = _sid.asStateFlow()

    init {
        loadSid()
    }

    private fun loadSid() {
        viewModelScope.launch {
            val storedSid = cookieStorage.getSid()

            _sid.value = storedSid.takeIf {
                it.isNotBlank()
            } ?: ""
        }
    }
}
package com.sensale.app.ui.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sensale.app.data.ChatMessage

class ChatViewModel : ViewModel() {

    var messages = mutableStateOf(
        listOf(ChatMessage("Merhaba! Eşleştik 🎉", isFromUser = false))
    )
        private set

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        messages.value = messages.value + ChatMessage(text, isFromUser = true)
    }
}

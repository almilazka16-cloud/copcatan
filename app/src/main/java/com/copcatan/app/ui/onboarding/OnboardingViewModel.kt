package com.copcatan.app.ui.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.copcatan.app.data.ChatMessage

/**
 * Şu an sabit (scripted) sorulardan oluşan bir onboarding akışı.
 * Faz 1'de bu, gerçek bir LLM (ör. Claude API) ile dinamik bir sohbete
 * dönüşecek ve cevaplardan profil verisi (ilgi alanları, kişilik özellikleri)
 * çıkarılacak.
 */
private val scriptedQuestions = listOf(
    "Merhaba! Ben CopCatan'ın AI asistanıyım. Seni tanımak için birkaç soru soracağım. " +
        "Önce, boş zamanlarında ne yapmayı seversin?",
    "Güzel! Peki, bir ilişkide senin için en önemli şey nedir?",
    "Son olarak, seni bir cümlede nasıl tanımlarsın?"
)

class OnboardingViewModel : ViewModel() {

    var messages = mutableStateOf(listOf(ChatMessage(scriptedQuestions[0], isFromUser = false)))
        private set

    var isComplete = mutableStateOf(false)
        private set

    private var currentQuestionIndex = 0

    fun submitAnswer(answer: String) {
        if (answer.isBlank()) return

        val updated = messages.value + ChatMessage(answer, isFromUser = true)
        currentQuestionIndex++

        messages.value = if (currentQuestionIndex < scriptedQuestions.size) {
            updated + ChatMessage(scriptedQuestions[currentQuestionIndex], isFromUser = false)
        } else {
            isComplete.value = true
            updated + ChatMessage(
                "Teşekkürler! Profilin oluşturuluyor. (Not: bu demo sürümünde cevapların henüz " +
                    "gerçek bir AI tarafından analiz edilmiyor — bu, sonraki fazda eklenecek.)",
                isFromUser = false
            )
        }
    }
}

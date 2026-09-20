package com.copcatan.app.ui.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.copcatan.app.data.ChatMessage

/**
 * Şu an sabit (scripted) sorulardan oluşan bir onboarding akışı.
 * Faz 1'de bu, gerçek bir LLM (ör. Claude API) ile dinamik bir sohbete
 * dönüşecek. Sorular bilerek iki gruba ayrıldı, çünkü docs/ARCHITECTURE.md'deki
 * uyum modeli iki ayrı vektöre dayanıyor: "kimlik" (ben kimim) ve "tercih"
 * (partnerde neyi önemsiyorum). Cevaplar ileride bu iki vektöre ayrı ayrı
 * beslenecek — bu yüzden burada da ayrı tutuluyor.
 */
private enum class QuestionType { IDENTITY, PREFERENCE }

private data class ScriptedQuestion(val text: String, val type: QuestionType)

private val scriptedQuestions = listOf(
    ScriptedQuestion(
        text = "Merhaba! Ben Sensale'nin AI asistanıyım. Seni tanımak için birkaç soru soracağım. " +
            "Önce, boş zamanlarında ne yapmayı seversin?",
        type = QuestionType.IDENTITY
    ),
    ScriptedQuestion(
        text = "Seni bir cümlede nasıl tanımlarsın?",
        type = QuestionType.IDENTITY
    ),
    ScriptedQuestion(
        text = "Şimdi de tam tersini soracağım: sen karşındaki kişide neyi önemsiyorsun? " +
            "Bir partnerde senin için en önemli şey nedir?",
        type = QuestionType.PREFERENCE
    ),
    ScriptedQuestion(
        text = "Ortak ilgi alanlarına mı, yoksa değerlerin örtüşmesine mi daha çok önem verirsin?",
        type = QuestionType.PREFERENCE
    )
)

class OnboardingViewModel : ViewModel() {

    var messages = mutableStateOf(
        listOf(ChatMessage(scriptedQuestions[0].text, isFromUser = false))
    )
        private set

    var isComplete = mutableStateOf(false)
        private set

    /** Faz 1'de backend'e gönderilecek ham veri: kimlik ve tercih cevapları ayrı tutulur. */
    var identityAnswers = mutableStateOf(listOf<String>())
        private set

    var preferenceAnswers = mutableStateOf(listOf<String>())
        private set

    private var currentQuestionIndex = 0

    fun submitAnswer(answer: String) {
        if (answer.isBlank()) return

        when (scriptedQuestions[currentQuestionIndex].type) {
            QuestionType.IDENTITY -> identityAnswers.value = identityAnswers.value + answer
            QuestionType.PREFERENCE -> preferenceAnswers.value = preferenceAnswers.value + answer
        }

        val updated = messages.value + ChatMessage(answer, isFromUser = true)
        currentQuestionIndex++

        messages.value = if (currentQuestionIndex < scriptedQuestions.size) {
            updated + ChatMessage(scriptedQuestions[currentQuestionIndex].text, isFromUser = false)
        } else {
            isComplete.value = true
            updated + ChatMessage(
                "Teşekkürler! Profilin oluşturuluyor. (Not: bu demo sürümünde cevapların henüz " +
                    "gerçek bir AI tarafından kimlik/tercih vektörüne dönüştürülmüyor — bu, " +
                    "sonraki fazda eklenecek.)",
                isFromUser = false
            )
        }
    }
}

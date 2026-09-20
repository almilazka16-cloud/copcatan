package com.sensale.app.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

enum class AuthStep { METHOD, EMAIL_INPUT, EMAIL_CODE, PHONE_INPUT, PHONE_CODE }

private const val CODE_LENGTH = 6

/**
 * Şu an tamamen mock: gerçek bir doğrulama kodu göndermiyor, doğru uzunluktaki
 * her kod kabul ediliyor. Faz 1'de Firebase Authentication (e-posta bağlantısı /
 * telefon SMS OTP) ile değiştirilecek — bkz. docs/ARCHITECTURE.md.
 */
class AuthViewModel : ViewModel() {

    var step = mutableStateOf(AuthStep.METHOD)
        private set

    var email = mutableStateOf("")
        private set

    var phoneNumber = mutableStateOf("")
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun chooseEmail() {
        errorMessage.value = null
        step.value = AuthStep.EMAIL_INPUT
    }

    fun choosePhone() {
        errorMessage.value = null
        step.value = AuthStep.PHONE_INPUT
    }

    fun backToMethodSelect() {
        errorMessage.value = null
        step.value = AuthStep.METHOD
    }

    fun submitEmail(value: String) {
        if (!value.contains("@") || !value.contains(".")) {
            errorMessage.value = "Geçerli bir e-posta adresi gir."
            return
        }
        errorMessage.value = null
        email.value = value
        step.value = AuthStep.EMAIL_CODE
    }

    fun submitPhone(value: String) {
        val digitsOnly = value.filter { it.isDigit() }
        if (digitsOnly.length < 10) {
            errorMessage.value = "Geçerli bir telefon numarası gir."
            return
        }
        errorMessage.value = null
        phoneNumber.value = value
        step.value = AuthStep.PHONE_CODE
    }

    fun submitCode(code: String, onVerified: () -> Unit) {
        if (code.filter { it.isDigit() }.length != CODE_LENGTH) {
            errorMessage.value = "$CODE_LENGTH haneli kodu eksiksiz gir."
            return
        }
        errorMessage.value = null
        onVerified()
    }
}

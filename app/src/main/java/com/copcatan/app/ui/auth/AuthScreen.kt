package com.copcatan.app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val step by viewModel.step
    val error by viewModel.errorMessage

    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (step) {
                AuthStep.METHOD -> MethodStep(
                    onEmail = viewModel::chooseEmail,
                    onPhone = viewModel::choosePhone
                )
                AuthStep.EMAIL_INPUT -> EmailInputStep(
                    onSubmit = viewModel::submitEmail,
                    onBack = viewModel::backToMethodSelect
                )
                AuthStep.EMAIL_CODE -> CodeStep(
                    destination = viewModel.email.value,
                    onSubmit = { code -> viewModel.submitCode(code, onAuthenticated) },
                    onBack = viewModel::backToMethodSelect
                )
                AuthStep.PHONE_INPUT -> PhoneInputStep(
                    onSubmit = viewModel::submitPhone,
                    onBack = viewModel::backToMethodSelect
                )
                AuthStep.PHONE_CODE -> CodeStep(
                    destination = viewModel.phoneNumber.value,
                    onSubmit = { code -> viewModel.submitCode(code, onAuthenticated) },
                    onBack = viewModel::backToMethodSelect
                )
            }

            if (error != null) {
                Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun MethodStep(onEmail: () -> Unit, onPhone: () -> Unit) {
    Text("CopCatan'a Hoş Geldin", style = MaterialTheme.typography.titleLarge)
    Text(
        "Devam etmek için e-posta veya telefon numaranla giriş yap.",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Button(onClick = onEmail, modifier = Modifier.fillMaxWidth()) {
        Text("E-posta ile devam et")
    }
    OutlinedButton(onClick = onPhone, modifier = Modifier.fillMaxWidth()) {
        Text("Telefon numarası ile devam et")
    }
}

@Composable
private fun EmailInputStep(onSubmit: (String) -> Unit, onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }

    Text("E-posta adresin", style = MaterialTheme.typography.titleLarge)
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("ornek@eposta.com") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )
    Button(onClick = { onSubmit(email) }, modifier = Modifier.fillMaxWidth()) {
        Text("Doğrulama Kodu Gönder")
    }
    TextButton(onClick = onBack) { Text("Geri") }
}

@Composable
private fun PhoneInputStep(onSubmit: (String) -> Unit, onBack: () -> Unit) {
    var phone by remember { mutableStateOf("") }

    Text("Telefon numaran", style = MaterialTheme.typography.titleLarge)
    OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("5xx xxx xx xx") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true
    )
    Button(onClick = { onSubmit(phone) }, modifier = Modifier.fillMaxWidth()) {
        Text("Doğrulama Kodu Gönder")
    }
    TextButton(onClick = onBack) { Text("Geri") }
}

@Composable
private fun CodeStep(destination: String, onSubmit: (String) -> Unit, onBack: () -> Unit) {
    var code by remember { mutableStateOf("") }

    Text("Kodu Gir", style = MaterialTheme.typography.titleLarge)
    Text(
        "$destination adresine/numarasına bir doğrulama kodu gönderildi. " +
            "(Demo: 6 haneli herhangi bir kod kabul edilir.)",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    OutlinedTextField(
        value = code,
        onValueChange = { code = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("123456") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
    Button(onClick = { onSubmit(code) }, modifier = Modifier.fillMaxWidth()) {
        Text("Doğrula")
    }
    TextButton(onClick = onBack) { Text("Geri") }
}

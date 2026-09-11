package com.copcatan.app.ui.matches

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copcatan.app.data.MockProfileRepository

@Composable
fun MatchesScreen(onOpenChat: (String) -> Unit) {
    val matches = MockProfileRepository.getMatches()

    if (matches.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Henüz bir eşleşmen yok. Keşfet sekmesinden profillere göz at!")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(matches, key = { it.id }) { profile ->
            ListItem(
                headlineContent = { Text(profile.name) },
                supportingContent = { Text("%${profile.compatibilityPercent} uyum") },
                modifier = Modifier.clickable { onOpenChat(profile.id) }
            )
            HorizontalDivider()
        }
    }
}

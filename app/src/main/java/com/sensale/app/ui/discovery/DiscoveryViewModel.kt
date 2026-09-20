package com.sensale.app.ui.discovery

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sensale.app.data.MockProfileRepository
import com.sensale.app.data.Profile

class DiscoveryViewModel : ViewModel() {

    var profiles = mutableStateOf(MockProfileRepository.getDiscoveryProfiles())
        private set

    fun onSwipe(profile: Profile) {
        profiles.value = profiles.value.filterNot { it.id == profile.id }
    }
}

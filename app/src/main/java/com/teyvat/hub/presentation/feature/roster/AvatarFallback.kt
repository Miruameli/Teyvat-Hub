package com.teyvat.hub.presentation.feature.roster

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.presentation.core.theme.TeyvatNightDeep
import com.teyvat.hub.presentation.core.theme.colorSwatch

/**
 * Shown where an avatar image could not be loaded.
 *
 * The portrait comes from a CDN, so an offline device or a renamed file
 * leaves the row without an image. The element colour carries the same
 * identity without another request, which keeps a row readable offline.
 */
@Composable
internal fun AvatarFallback(character: Character) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(character.element.colorSwatch()),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = character.name.take(1).uppercase(),
            color = TeyvatNightDeep,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

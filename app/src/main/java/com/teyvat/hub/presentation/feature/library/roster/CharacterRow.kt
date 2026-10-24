package com.teyvat.hub.presentation.feature.library.roster

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teyvat.hub.R
import com.teyvat.hub.domain.character.Character
import com.teyvat.hub.presentation.core.avatar.EnkaAsset
import com.teyvat.hub.presentation.core.theme.labelRes

/**
 * One character in the roster, with the favourite toggle.
 *
 * The card itself carries no click handler: the star owns the only tap, so a
 * press can never fire the same action twice.
 */
@Composable
internal fun CharacterRow(
    character: Character,
    onToggleFavorite: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Coil's AsyncImage has no composable error slot in 3.x, so the
            // coloured swatch is layered behind the portrait and shows through
            // whenever the CDN cannot deliver the image.
            Box(modifier = Modifier.size(56.dp)) {
                AvatarFallback(character)
                AsyncImage(
                    model = EnkaAsset.avatarSide(character.name),
                    contentDescription = character.name,
                    modifier = Modifier.size(56.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(
                        R.string.element_region_pair,
                        stringResource(character.element.labelRes()),
                        character.region,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (character.isFavorite) {
                        Icons.Filled.Star
                    } else {
                        Icons.Outlined.StarBorder
                    },
                    contentDescription = stringResource(
                        if (character.isFavorite) {
                            R.string.marker_favorite_on
                        } else {
                            R.string.marker_favorite_off
                        },
                    ),
                )
            }
        }
    }
}

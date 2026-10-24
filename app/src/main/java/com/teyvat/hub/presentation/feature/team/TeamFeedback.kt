package com.teyvat.hub.presentation.feature.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teyvat.hub.R
import com.teyvat.hub.domain.team.SynergyInsight
import com.teyvat.hub.domain.team.TeamInvalidRule
import com.teyvat.hub.presentation.core.theme.MAX_PARTY_SIZE
import com.teyvat.hub.presentation.core.theme.detailRes
import com.teyvat.hub.presentation.core.theme.headlineRes
import com.teyvat.hub.presentation.core.theme.messageRes

/**
 * Feedback about the team being built: why it cannot be saved yet, and what
 * its composition achieves.
 *
 * Both read from domain rules, so the wording follows the chosen language.
 */

/** Reason the current selection cannot be saved. */
@Composable
fun ValidationError(rule: TeamInvalidRule) {
    Text(
        text = if (rule == TeamInvalidRule.TOO_LARGE) {
            // The size rule is the only reason that quotes a number, and the
            // number comes from the domain so the wording cannot drift from
            // the rule that produced it.
            stringResource(rule.messageRes(), MAX_PARTY_SIZE)
        } else {
            stringResource(rule.messageRes())
        },
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

/** Actionable notes about the current party composition. */
@Composable
fun SynergyNotes(insights: List<SynergyInsight>) {
    if (insights.isEmpty()) return

    SectionHeading(stringResource(R.string.teams_synergy))

    Column(modifier = Modifier.padding(start = 16.dp)) {
        insights.forEach { insight ->
            val rule = insight.rule
            Text(
                text = stringResource(rule.headlineRes()),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(rule.detailRes()),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

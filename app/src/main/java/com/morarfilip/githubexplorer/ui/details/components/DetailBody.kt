package com.morarfilip.githubexplorer.ui.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.morarfilip.githubexplorer.core.common.DateFormatter
import com.morarfilip.githubexplorer.ui.navigation.RepositoryDetailRoute
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme
import com.morarfilip.githubexplorer.ui.util.RepositoryPreviewProvider

@Composable
fun DetailBody(
    detail: RepositoryDetailRoute,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Repository Name
        Text(
            text = detail.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // 2. Language Badge
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = detail.language,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        // 3. Stats Dashboard (2x2 Grid)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatItem(
                        label = "Stars",
                        value = detail.stars.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatItem(
                        label = "Forks",
                        value = detail.forks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatItem(
                        label = "Watchers",
                        value = detail.watchers.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatItem(
                        label = "Issues",
                        value = detail.openIssues.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Owner & License Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoLabel(
                label = "Owner",
                value = detail.ownerName
            )
            InfoLabel(
                label = "License",
                value = detail.license.ifBlank {
                    "No License"
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. About / Description Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = detail.description.ifBlank {
                    "No description available for this repository."
                },
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 6. Footer Timestamp
        Text(
            text = "Last Updated: ${DateFormatter.formatIsoDate(detail.updatedAt)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            softWrap = true
        )
    }
}

@Preview(showBackground = true, name = "Detail Body Light")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Detail Body Dark"
)
@Composable
fun PreviewDetailBody(
    @PreviewParameter(RepositoryPreviewProvider::class) detail: RepositoryDetailRoute
) {
    GithubExplorerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                DetailBody(detail = detail)
            }
        }
    }
}
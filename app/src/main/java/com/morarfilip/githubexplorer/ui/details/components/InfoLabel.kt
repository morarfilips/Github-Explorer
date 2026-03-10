package com.morarfilip.githubexplorer.ui.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme

@Composable
fun InfoLabel(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, name = "Info Label Light")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Info Label Dark"
)
@Composable
fun PreviewInfoLabel() {
    GithubExplorerTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            InfoLabel(
                label = "License",
                value = "Apache 2.0"
            )
        }
    }
}
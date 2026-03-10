package com.morarfilip.githubexplorer.ui.details

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.morarfilip.githubexplorer.ui.details.components.DetailAvatar
import com.morarfilip.githubexplorer.ui.details.components.DetailBody
import com.morarfilip.githubexplorer.ui.navigation.RepositoryDetailRoute
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme
import com.morarfilip.githubexplorer.ui.util.RepositoryPreviewProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    detail: RepositoryDetailRoute,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = detail.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    ) { padding ->
        BoxWithConstraints(modifier = Modifier.padding(padding)) {
            val isExpanded = maxWidth > 600.dp

            if (isExpanded) {
                // TABLET or PHONE / LANDSCAPE
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) {
                    DetailAvatar(
                        url = detail.ownerAvatarUrl,
                        modifier = Modifier.weight(1f),
                        size = 280.dp
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetailBody(detail)
                    }
                }
            } else {
                // PHONE / PORTRAIT
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DetailAvatar(url = detail.ownerAvatarUrl)
                    Spacer(modifier = Modifier.height(24.dp))
                    DetailBody(detail = detail)
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp",
    name = "Phone Portrait - Light"
)
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Phone Portrait - Dark"
)
@Composable
fun PreviewDetailScreenPhone(
    @PreviewParameter(RepositoryPreviewProvider::class) detail: RepositoryDetailRoute
) {
    GithubExplorerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RepositoryDetailScreen(detail = detail, onBack = {})
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    name = "Tablet Landscape - Light"
)
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Tablet Landscape - Dark"
)
@Composable
fun PreviewDetailScreenTablet(
    @PreviewParameter(RepositoryPreviewProvider::class) detail: RepositoryDetailRoute
) {
    GithubExplorerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RepositoryDetailScreen(detail = detail, onBack = {})
        }
    }
}
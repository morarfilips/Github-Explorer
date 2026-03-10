package com.morarfilip.githubexplorer.ui.details.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DetailAvatar(
    url: String,
    repoId: Long,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = url,
                contentDescription = "Owner Avatar",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "avatar-$repoId"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .size(size)
                    .clip(CircleShape)
            )
        }
    }
}
package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage



/**
 * Circular profile image component (alias for [ProfileImage]).
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircularProfileImage(
    model: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    contentDescription: String? = "Profile Image",
    contentScale: ContentScale = ContentScale.Crop,
    placeholderPainter: Painter? = rememberVectorPainter(Icons.Default.Person)
) {

    val imageModifier = modifier
        .size(size)
        .clip(CircleShape)

    if (model == null || model.isBlank()) {
        ProfileImagePlaceholder(
            modifier = imageModifier,
            contentDescription = contentDescription,
            painter = placeholderPainter
        )
    } else {
        GlideImage(
            model = model,
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = contentScale,
        )
    }

}

@Composable
private fun ProfileImagePlaceholder(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    painter: Painter? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    iconTintColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Box(
        modifier = modifier.background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        painter?.let {
            Icon(
                painter = it,
                contentDescription = contentDescription,
                tint = iconTintColor,
            )
        }
    }
}

@Preview(name = "Profile Image Default 36dp", showBackground = true)
@Composable
private fun ProfileImagePreview() {
    MaterialTheme {
        CircularProfileImage(
            model = "https://i.pravatar.cc/250?img=70"
        )
    }
}

@Preview(name = "Profile Image Placeholder", showBackground = true)
@Composable
private fun ProfileImagePlaceholderPreview() {
    MaterialTheme {
        CircularProfileImage(
            model = null
        )
    }
}

@Preview(name = "Profile Image Custom Sizes", showBackground = true)
@Composable
private fun ProfileImageCustomSizesPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color.White)) {
            CircularProfileImage(
                model = null,
                size = 56.dp
            )
        }
    }
}

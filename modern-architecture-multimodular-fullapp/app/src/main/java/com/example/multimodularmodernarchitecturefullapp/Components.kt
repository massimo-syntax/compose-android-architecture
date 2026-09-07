package com.example.multimodularmodernarchitecturefullapp


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ============ TYPOGRAPHY ============

@Composable
fun TitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        letterSpacing = (-0.5).sp
    )
}

@Composable
fun TitleMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = color
    )
}

@Composable
fun BodyLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = color,
        lineHeight = 24.sp
    )
}

@Composable
fun BodySmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = color,
        lineHeight = 18.sp
    )
}

// ============ MODERN TOP BAR ============

@Composable
fun ModernTopBar(
    title: String,
    leadingIcon: ImageVector? = Icons.Default.ArrowBack,
    trailingIcon: ImageVector? = Icons.Default.MoreVert,
    onLeadingClick: () -> Unit = {},
    onTrailingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Leading Icon
            if (leadingIcon != null) {
                IconButton(
                    onClick = onLeadingClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = "Leading Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Title (centered, flexible)
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Trailing Icon
            if (trailingIcon != null) {
                IconButton(
                    onClick = onTrailingClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = "Trailing Icon",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}

// ============ MODERN LIST ITEM ============

@Composable
fun ModernListItem(
    title: String,
    subtitle: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surface
            ),
        color = if (isHighlighted) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Leading Content (icon, avatar, etc.)
            if (leadingContent != null) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    leadingContent()
                }
            }

            // Title & Subtitle Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Trailing Content (icon, badge, etc.)
            if (trailingContent != null) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    trailingContent()
                }
            }
        }
    }

    // Divider
    Divider(
        modifier = Modifier.padding(start = 76.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        thickness = 0.5.dp
    )
}

// ============ BANNER ITEM WITH BACKGROUND IMAGE ============

@Composable
fun BannerCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    overlayOpacity: Float = 0.4f
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            AsyncImage(
                model = imageUrl,
                contentDescription = "Banner Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark Overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Color.Black.copy(
                            alpha = overlayOpacity
                        )
                    )
            )

            // Content: Title & Subtitle
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Accent Border (optional modern touch)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

// ============ BANNER CARD VARIANT - WITH ACCENT COLOR ============

@Composable
fun AccentBannerCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image with blur/gradient effect
            AsyncImage(
                model = imageUrl,
                contentDescription = "Banner Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay (bottom-heavy for text readability)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground,
                                MaterialTheme.colorScheme.onSurface
                            )
                        )
                    )
            )


            // Dark Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Accent Bar at top
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .background(accentColor)
                        .clip(RoundedCornerShape(2.dp))
                )

                // Title & Subtitle at bottom
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

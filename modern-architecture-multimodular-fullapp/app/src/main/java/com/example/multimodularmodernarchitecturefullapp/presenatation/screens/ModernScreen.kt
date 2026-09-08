package com.example.multimodularmodernarchitecturefullapp.presenatation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.presentation.BannerCard
import com.example.designsystem.presentation.BodyLarge
import com.example.designsystem.presentation.BodySmall
import com.example.designsystem.presentation.ModernListItem
import com.example.designsystem.presentation.ModernTopBar
import com.example.designsystem.presentation.TitleLarge
import com.example.designsystem.presentation.TitleMedium

@Composable
fun ModernScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Modern Top Bar
        ModernTopBar(
            title = "Modern Components",
            leadingIcon = Icons.Default.ArrowBack,
            trailingIcon = Icons.Default.MoreVert,
            onLeadingClick = { /* Handle back */ },
            onTrailingClick = { /* Handle menu */ }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Typography Examples
            item {
                TitleLarge(
                    text = "Explore Modern UI",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                BodySmall(text = "Chic components with vibrant colors")
            }

            // Banner Cards
            item {
                BannerCard(
                    title = "Beautiful Design",
                    subtitle = "Modern & Elegant",
                    imageUrl = "https://images.unsplash.com/photo-1557672172-298e090d0f80?w=400&h=300&fit=crop",
                    onClick = { /* Handle click */ }
                )
            }


            // Section: Typography
            item {
                TitleMedium(
                    text = "Typography Styles",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TitleLarge(
                            text = "Big Title (32sp Bold)",
                            color = MaterialTheme.colorScheme.primary
                        )

                        TitleMedium(
                            text = "Medium Title (24sp SemiBold)",
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        BodyLarge(
                            text = "Large Body Text (16sp Normal) - This is the standard body text for main content with comfortable line height for readability.",
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        BodySmall(
                            text = "Small Body Text (12sp Normal) - Used for secondary information and captions",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section: List Items
            item {
                TitleMedium(
                    text = "List Items",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(
                listOf(
                    Pair("Jetpack Compose", "Build Android UI with Kotlin"),
                    Pair("Material 3", "Modern design system"),
                    Pair("Coil Library", "Image loading made easy"),
                    Pair("Kotlin Coroutines", "Async programming simplified")
                )
            ) { (title, subtitle) ->
                ModernListItem(
                    title = title,
                    subtitle = subtitle,
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = { /* Handle item click */ }
                )
            }

            // Section: Color Palette
            item {
                TitleMedium(
                    text = "Color Palette",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ColorBox("Primary", MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    ColorBox("Secondary", MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                    ColorBox("Tertiary", MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ColorBox("Success", Color(0xFF00E676), Modifier.weight(1f))
                    ColorBox("Warning", Color(0xFFFFC107), Modifier.weight(1f))
                    ColorBox("Error", MaterialTheme.colorScheme.error, Modifier.weight(1f))
                }
            }

            // Section: Interactive List
            item {
                TitleMedium(
                    text = "Interactive Components",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                ModernListItem(
                    title = "Settings",
                    subtitle = "Manage your preferences",
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = { /* Navigate to settings */ },
                    isHighlighted = false
                )
            }

            item {
                ModernListItem(
                    title = "Notifications",
                    subtitle = "3 unread messages",
                    leadingContent = {
                        Badge(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    trailingContent = {
                        Surface(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "3",
                                modifier = Modifier.padding(4.dp, 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    },
                    onClick = { /* Navigate to notifications */ },
                    isHighlighted = true
                )
            }

            item {
                ModernListItem(
                    title = "Profile",
                    subtitle = "View your account",
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = { /* Navigate to profile */ }
                )
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Helper Composable for Color Boxes
@Composable
fun ColorBox(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = color,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp
        ) {}

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

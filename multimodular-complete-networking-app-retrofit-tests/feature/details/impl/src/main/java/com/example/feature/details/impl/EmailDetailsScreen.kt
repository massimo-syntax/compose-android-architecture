package com.example.feature.details.impl

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.networkresult.NetworkError
import com.example.domain.model.emaildetails.EmailDetails
import com.example.ui.components.CircularProfileImage
import com.example.ui.components.DetailTopBar
import com.example.ui.components.ErrorScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EmailDetailsScreen(
    viewModel: EmailDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState !is EmailDetailsState.Success) {
            viewModel.event(EmailDetailsEvent.LoadEmail)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is EmailDetailsEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DetailTopBar(
            onBackClick = {
                viewModel.event(EmailDetailsEvent.BackClicked)
            }
        )

        when (uiState) {
            is EmailDetailsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                }
            }

            is EmailDetailsState.Error -> {
                val error = (uiState as EmailDetailsState.Error).error
                ErrorScreen(message = error.toErrorMessage())
            }

            is EmailDetailsState.Success -> {
                val email = (uiState as EmailDetailsState.Success).data
                EmailDetailsContent(
                    email = email,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun EmailDetailsContent(
    email: EmailDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = email.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProfileImage(
                model = email.image,
                size = 40.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = email.payload?.from ?: "Unknown Sender",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                email.payload?.email?.let { senderEmail ->
                    Text(
                        text = senderEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadDataWithBaseURL(null, email.body, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(null, email.body, "text/html", "UTF-8", null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

private fun NetworkError.toErrorMessage(): String {
    return when (this) {
        is NetworkError.NoInternet -> "No internet connection"
        is NetworkError.Timeout -> "Request timed out"
        is NetworkError.Server -> "Server error"
        is NetworkError.Unauthorized -> "Unauthorized"
        is NetworkError.Forbidden -> "Forbidden"
        is NetworkError.NotFound -> "Email details not found"
        is NetworkError.Serialization -> "Data parsing error"
        is NetworkError.Http -> "HTTP Error $code: ${message.orEmpty()}"
        is NetworkError.Unknown -> cause.message ?: "An unknown error occurred"
    }
}

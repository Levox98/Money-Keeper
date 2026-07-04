package com.moneykeeper.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moneykeeper.R
import com.moneykeeper.ui.component.AiAnalysisResultCard
import com.moneykeeper.ui.viewmodel.FundsViewModel

@Composable
fun AIScreen(viewModel: FundsViewModel) {
    val aiAnalysis by viewModel.aiAnalysis.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

    AIScreenContent(
        aiAnalysis = aiAnalysis,
        isAnalyzing = isAnalyzing,
        onRunAnalysis = { viewModel.runAiAnalysis() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreenContent(
    aiAnalysis: String?,
    isAnalyzing: Boolean,
    onRunAnalysis: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ai_analysis)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onRunAnalysis,
                enabled = !isAnalyzing,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.analyzing))
                } else {
                    Text(stringResource(R.string.run_analysis))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (aiAnalysis != null) {
                AiAnalysisResultCard(analysis = aiAnalysis)
            } else if (!isAnalyzing) {
                Text(
                    text = "Нажмите на кнопку выше, чтобы ИИ проанализировал ваши финансы и дал советы.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

package com.moneykeeper.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.moneykeeper.R
import com.moneykeeper.ui.theme.MoneyKeeperTheme

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_category)) },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.category_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name) }
            ) { Text(stringResource(R.string.add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddCategoryDialogPreview() {
    MoneyKeeperTheme {
        AddCategoryDialog(onDismiss = {}, onConfirm = {})
    }
}

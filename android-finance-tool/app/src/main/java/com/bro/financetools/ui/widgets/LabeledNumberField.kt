package com.bro.financetools.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun LabeledNumberField(
  labelRes: Int,
  value: String,
  onValueChange: (String) -> Unit,
  suffix: String? = null,
  descriptionRes: Int? = null,
  modifier: Modifier = Modifier,
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = {
      Text(
        text = buildString {
          append(stringResource(labelRes))
          if (suffix != null) append(" (" + suffix + ")")
        }
      )
    },
    supportingText = {
      if (descriptionRes != null) Text(stringResource(descriptionRes))
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    modifier = modifier.fillMaxWidth(),
    singleLine = true,
  )
}

package com.example.lamborghiniandroid.todo.ui.car

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarScreen(
    carId: String?,
    onClose: () -> Unit
) {
    val vm: CarViewModel = viewModel(factory = CarViewModel.Factory)
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(carId) {
        val id = carId?.toLongOrNull()
        vm.load(id)
    }

    val modelState = remember(state.model) { mutableStateOf(state.model) }
    val context = LocalContext.current

    val dateText = state.manufacturingDate

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (carId == null) "New car" else "Edit car") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(4.dp)
                        .padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = modelState.value,
                onValueChange = {
                    modelState.value = it
                    vm.setModel(it)
                },
                label = { Text("Model") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable {
                        val cal = Calendar.getInstance()

                        if (dateText.isNotBlank()) {
                            try {
                                val ld = LocalDate.parse(dateText)
                                cal.set(Calendar.YEAR, ld.year)
                                cal.set(Calendar.MONTH, ld.monthValue - 1)
                                cal.set(Calendar.DAY_OF_MONTH, ld.dayOfMonth)
                            } catch (_: Exception) {
                            }
                        }

                        val year = cal.get(Calendar.YEAR)
                        val month = cal.get(Calendar.MONTH)
                        val day = cal.get(Calendar.DAY_OF_MONTH)

                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                val selectedDate = LocalDate.of(y, m + 1, d)
                                vm.setDate(selectedDate.toString()) // "YYYY-MM-DD"
                            },
                            year,
                            month,
                            day
                        ).show()
                    }
            ) {
                OutlinedTextField(
                    value = dateText,
                    onValueChange = {},
                    label = { Text("Manufacturing date") },
                    enabled = false, // nu vrem tastatura
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Checkbox(
                checked = state.owned,
                onCheckedChange = { vm.setOwned(it) }
            )
            Text("Owned")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { vm.save(onClose) }
            ) {
                Text("Save")
            }

            if (state.car?.id != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { vm.delete(onClose) },
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text("Delete")
                }
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Error: ${state.error}")
            }
        }
    }
}

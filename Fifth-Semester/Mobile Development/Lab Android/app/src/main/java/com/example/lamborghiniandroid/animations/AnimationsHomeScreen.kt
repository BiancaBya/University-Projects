package com.example.lamborghiniandroid.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class TabPage { Home, Work }

@Composable
fun AnimationsHomeScreen() {
    var tabPage by remember { mutableStateOf(TabPage.Home) }
    var isEditing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun showEditMessage() {
        if (!isEditing) {
            isEditing = true
            delay(3000L)
            isEditing = false
        }
    }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tabPage.ordinal) {
                Tab(
                    selected = tabPage == TabPage.Home,
                    onClick = { tabPage = TabPage.Home },
                    text = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                Tab(
                    selected = tabPage == TabPage.Work,
                    onClick = { tabPage = TabPage.Work },
                    text = { Text("Work") },
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = null) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { coroutineScope.launch { showEditMessage() } }
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    AnimatedVisibility(visible = !isEditing) {
                        Text(text = "Edit", modifier = Modifier.padding(start = 8.dp, top = 3.dp))
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            modifier = Modifier.padding(padding)
        ) {
            item { Text("Todo $tabPage") }
            item { MyAsyncItem() }
            item { MyExpandableItem() }
        }

        EditMessage(shown = isEditing)
    }
}

@Composable
private fun EditMessage(shown: Boolean) {
    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = Modifier,
            tonalElevation = 4.dp
        ) {
            Text(
                text = "Edit is not available",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}



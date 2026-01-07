package ru.ildar.culturalexplorer

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ildar.designsystem.R
import ru.ildar.designsystem.CulturalExplorerTheme
import ru.ildar.designsystem.components.FeatureCard
import ru.ildar.domain.model.FeatureItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onBookMoodClick: () -> Unit,
    onQuoteCreatorClick: () -> Unit,
) {
    val features = listOf(
        FeatureItem("book", R.string.feature_book_title_main, R.drawable.ic_book, onBookMoodClick),
        FeatureItem("quote", R.string.feature_quote_title, R.drawable.ic_quote, onQuoteCreatorClick)
    )

    LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(features) { item ->
                FeatureCard(
                    title = stringResource(item.titleResId),
                    icon = painterResource(item.iconResId),
                    onClick = item.onClick
                )
            }
        }
    }

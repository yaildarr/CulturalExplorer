package ru.ildar.book_impl.bookdetail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.ildar.designsystem.R

@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    navController : NavHostController,
    viewModel: BookDetailViewModel = koinViewModel()
) {

    val arguments = navController.currentBackStackEntry?.arguments
    val state = viewModel.collectAsState().value


    LaunchedEffect(Unit) {
        viewModel.onAction(BookDetailAction.DetailsOpen(arguments?.getString("bookId","") ?: ""))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))

    ) {

        state.book.coverId?.let { coverId ->
            AsyncImage(
                model = "https://covers.openlibrary.org/b/id/$coverId-L.jpg",
                contentDescription = state.book.title,
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = state.book.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        state.book.firstPublishYear?.let {
            Text(
                text = "Дата публикации: $it",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        state.book.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (state.book.subjects.isNotEmpty()) {
            Text(
                text = "Subjects",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.book.subjects.take(10).forEach {
                    AssistChip(
                        onClick = {},
                        label = { Text(it) }
                    )
                }
            }
        }
    }
}

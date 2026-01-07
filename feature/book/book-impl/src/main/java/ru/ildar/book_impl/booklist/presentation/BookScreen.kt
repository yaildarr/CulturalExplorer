package ru.ildar.book_impl.booklist.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.ildar.book_impl.BookItemPlaceholder
import ru.ildar.book_impl.BookListItem
import ru.ildar.book_impl.R
import ru.ildar.domain.model.Book


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onBookClick: (Book: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var query by remember { mutableStateOf(state.query) }

    LaunchedEffect(state.query) {
        query = state.query
    }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is BookSideEffect.ShowError -> {
            }
        }
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(ru.ildar.designsystem.R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.feature_book_title),
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = ru.ildar.designsystem.R.dimen.padding_large)))

            // Поле поиска
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.onAction(BookAction.QueryChanged(it))
                },
                label = { Text(stringResource(R.string.search_books_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.onAction(BookAction.SearchClicked)
                    }
                ),
                trailingIcon = {
                    if (query.isNotBlank()) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search",
                            modifier = Modifier.clickable {
                                viewModel.onAction(BookAction.SearchClicked)
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(7) {
                        BookItemPlaceholder(Modifier.fillMaxWidth())
                    }
                }
            } else if (state.books.isEmpty()) {
                if (state.query.isNotEmpty() && state.hasSearched) {
                    Text(
                        text = stringResource(R.string.no_books_found),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = stringResource(R.string.enter_query_to_search),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = state.books) { books ->
                        BookListItem(
                            book = books,
                            onClick = { onBookClick(books.id) }
                        )
                    }
                }
            }
        }

    LaunchedEffect(Unit) {
        scope.launch {
            focusRequester.requestFocus()
        }
    }
}


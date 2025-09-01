package com.example.mobilecodingstyleguideline.ui.screen.home.view.listsection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apiservices.data.model.SupplierEntity
import com.example.mobilecodingstyleguideline.model.home.HomeCallback
import com.example.mobilecodingstyleguideline.ui.screen.home.uistate.HomeUiState
import com.tagsamurai.tscomponents.pullrefresh.PullRefresh
import com.tagsamurai.tscomponents.screen.EmptyState
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.paddingList

@Composable
fun HomeListSection(
    uiState: HomeUiState,
    homeCallback: HomeCallback,
    onNavigateTo: (String) -> Unit,
    onEditAsset: (SupplierEntity) -> Unit
) {
    when {
        uiState.isLoading -> {
            Column(
                Modifier.padding(paddingList)
            ) {
                repeat(5) {
                    HomeLoadingItem()
                    4.heightBox()
                }
            }
        }

        else -> {
            if (uiState.supplier.isEmpty()) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    EmptyState()
                }
            } else {
                PullRefresh(
                    onRefresh = homeCallback.onRefresh
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingList,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(uiState.supplier.size) { index ->
                            HomeItem(
                                uiState = uiState,
                                homeCallback = homeCallback,
                                item = uiState.supplier[index],
                                onNavigateTo = onNavigateTo,
                                onEditAsset = onEditAsset
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeListSectionPreview() {
    HomeListSection(
        uiState = HomeUiState(
//            supplies = DataDummy.getAssets()
        ),
        homeCallback = HomeCallback(),
        onNavigateTo = {},
        onEditAsset = {}
    )
}
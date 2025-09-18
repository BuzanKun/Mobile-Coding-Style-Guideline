package com.example.mobilecodingstyleguideline.ui.screen.home.view.listsection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tagsamurai.tscomponents.R
import com.tagsamurai.tscomponents.card.AdaptiveCardItem
import com.tagsamurai.tscomponents.shimmerEffect.ShimmerEffect
import com.tagsamurai.tscomponents.utils.Spacer.heightBox
import com.tagsamurai.tscomponents.utils.Spacer.widthBox
import com.tagsamurai.tscomponents.utils.itemGap4

@Composable
fun HomeLoadingItem() {
    AdaptiveCardItem(
        showMoreIcon = true
    ) {
        Column {
            ShimmerEffect(width = 40.dp)
            itemGap4.heightBox()
            ShimmerEffect(width = 120.dp)
            itemGap4.heightBox()
            ShimmerEffect(width = 120.dp)
            itemGap4.heightBox()
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) {
                    ShimmerEffect(width = 58.dp)
                    itemGap4.widthBox()
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    ShimmerEffect(width = 120.dp)
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_user_line_24dp),
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                ShimmerEffect(width = 80.dp)
            }
        }
    }
}

@Preview
@Composable
private fun HomeLoadingItemPreview() {
    HomeLoadingItem()
}
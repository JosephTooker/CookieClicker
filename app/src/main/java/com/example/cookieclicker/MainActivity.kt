package com.example.cookieclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cookieclicker.data.Datasource
import com.example.cookieclicker.model.Cookie
import com.example.cookieclicker.ui.theme.CookieClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookieClickerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                ) {
                    CookieClickerApp(cookies = Datasource().cookieList)
                }
            }
        }
    }
}

fun determineDessertToShow(
    cookies: List<Cookie>,
    cookiesSold: Int
): Cookie {
    var cookieToShow = cookies.first()
    for (cookie in cookies) {
        if (cookiesSold >= cookie.stock) {
            cookieToShow = cookie
        } else {
            break
        }
    }

    return cookieToShow
}

@Composable
private fun CookieClickerApp(
    cookies: List<Cookie>
) {

    var revenue by remember { mutableStateOf(0) }
    var cookiesSold by remember { mutableStateOf(0) }
    val currentCookieIndex by remember { mutableStateOf(0) }
    var currentCookiePrice by remember { mutableStateOf(cookies[currentCookieIndex].price) }
    var currentCookieImageId by remember { mutableStateOf(cookies[currentCookieIndex].imageId) }

    Scaffold() { contentPadding ->
        DessertClickerScreen(
            revenue = revenue,
            cookiesClicked = cookiesSold,
            cookieImageId = currentCookieImageId,
            onCookieClicked = {

                // Update the revenue
                revenue += currentCookiePrice
                cookiesSold++

                // Show the next dessert
                val cookieToShow = determineDessertToShow(cookies, cookiesSold)
                currentCookieImageId = cookieToShow.imageId
                currentCookiePrice = cookieToShow.price
            },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun DessertClickerScreen(
    revenue: Int,
    cookiesClicked: Int,
    @DrawableRes cookieImageId: Int,
    onCookieClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(cookieImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .align(Alignment.Center)
                        .clickable { onCookieClicked() },
                    contentScale = ContentScale.Crop,
                )
            }
            TransactionInfo(
                revenue = revenue,
                cookiesClicked = cookiesClicked,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }
}

@Composable
fun TransactionInfo(
    revenue: Int,
    cookiesClicked: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CookiesClickedInfo(
            cookiesClicked = cookiesClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        RevenueInfo(
            revenue = revenue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun CookiesClickedInfo(cookiesClicked: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.cookies_clicked),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = cookiesClicked.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun RevenueInfo(revenue: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.total_revenue),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = "$${revenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview
@Composable
fun MyDessertClickerAppPreview() {
    CookieClickerTheme {
        CookieClickerApp(listOf(Cookie(R.drawable.chocolatechip, 5, 0)))
    }
}
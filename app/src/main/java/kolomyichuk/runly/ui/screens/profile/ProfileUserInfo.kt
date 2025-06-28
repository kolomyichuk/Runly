package kolomyichuk.runly.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import kolomyichuk.runly.R
import kolomyichuk.runly.data.model.UserProfile
import okhttp3.OkHttpClient

@Composable
fun ProfileUserInfo(
    profile: UserProfile
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = { OkHttpClient() }))
            }.build()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!profile.photoUrl.isNullOrBlank()) {
            AsyncImage(
                model = profile.photoUrl,
                imageLoader = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onError = { println("Image load error: ${it.result.throwable} ") },
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.user),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)

            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = profile.name ?: stringResource(R.string.user),
            fontSize = 18.sp,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
package com.nmichail.wordly.android.shared.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.isAppInDarkTheme

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    titleColor: Color = Color.White,
) {
    val logoRes = if (isAppInDarkTheme()) {
        R.drawable.logo_dark
    } else {
        R.drawable.logo_light
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(logoRes),
            contentDescription = stringResource(R.string.logo_app_name),
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = stringResource(R.string.logo_app_name),
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = titleColor,
            modifier = Modifier.padding(start = 10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LogoPreview(
    @PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
    WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp),
        ) {
            Logo()
        }
    }
}
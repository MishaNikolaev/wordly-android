package com.nmichail.wordly.android.features.authorization.signin.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.authorization.signin.R
import com.nmichail.wordly.android.shared.authorization.AuthPhoneticWordCard
import com.nmichail.wordly.android.shared.authorization.Logo
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun SignInAuthHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp)
			.padding(top = 12.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Logo()
        Box(
            modifier = Modifier
				.fillMaxWidth()
				.padding(top = 28.dp)
				.graphicsLayer { clip = false },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
					.width(360.dp)
					.height(180.dp),
            ) {
                AuthPhoneticWordCard(
                    word = stringResource(R.string.auth_preview_resilience_word),
                    phonetic = stringResource(R.string.auth_preview_resilience_phonetic),
                    rotation = 8f,
                    modifier = Modifier
						.align(Alignment.TopStart)
						.offset(y = 24.dp),
                )
                AuthPhoneticWordCard(
                    word = stringResource(R.string.auth_preview_deploy_word),
                    phonetic = stringResource(R.string.auth_preview_deploy_phonetic),
                    rotation = -6f,
                    modifier = Modifier
						.align(Alignment.TopStart)
						.offset(x = 148.dp, y = (-44).dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 360)
@Composable
private fun SignInAuthHeaderPreview() {
    WuiTheme {
        Box(
            modifier = Modifier
				.fillMaxWidth()
				.background(MaterialTheme.colorScheme.primary),
        ) {
            SignInAuthHeader()
        }
    }
}
package com.lihan.smartstep.coach.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.ButtonPrimary
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.core.presentation.ui.theme.TextPrimary
import com.lihan.smartstep.core.presentation.ui.theme.TextWhite
import com.lihan.smartstep.core.presentation.ui.theme.regular

@Composable
fun MessageCard(
    isUser: Boolean,
    message: String,
    modifier: Modifier = Modifier,
    userShape: Shape = RoundedCornerShape(16.dp,0.dp,16.dp,16.dp),
    aiShape: Shape = RoundedCornerShape(0.dp,16.dp,16.dp,16.dp)
) {
    val textColor = remember(isUser){
        if (isUser){
            TextWhite
        }else{
            TextPrimary
        }
    }
    Box(
        modifier = modifier
    ){
        Row(
            modifier = Modifier.align(
                if (isUser){
                    Alignment.CenterEnd
                }else{
                    Alignment.CenterStart
                }
            )
        ){
            if (!isUser){
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ButtonPrimary,CircleShape)
                        .padding(6.dp)
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.robot),
                        contentDescription = stringResource(R.string.ai_robot),
                        tint = Color.White
                    )
                }
                Spacer(Modifier.width(8.dp))
            }
            Box(
                modifier = Modifier
                    .then(
                        if (isUser){
                            Modifier
                                .clip(userShape)
                                .background(ButtonPrimary,userShape)
                        }else{
                            Modifier
                                .clip(aiShape)
                                .border(
                                    width = 1.dp,
                                    color = StrokeMain,
                                    shape = aiShape
                                )
                                .background(BackgroundWhite,aiShape)
                        }
                    )
                    .padding(16.dp)
            ){
                Text(
                    text = message,
                    style = MaterialTheme.typography.regular.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = textColor
                    )
                )
            }
        }

    }

}

@Preview
@Composable
private fun MessageCardPreview() {
    SmartStepTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            MessageCard(
                modifier = Modifier.fillMaxWidth(),
                isUser = true,
                message = "This is user's message"
            )
            Spacer(Modifier.height(16.dp))

            MessageCard(
                modifier = Modifier.fillMaxWidth(),
                isUser = false,
                message = "This is AI's message"
            )


        }
    }
}
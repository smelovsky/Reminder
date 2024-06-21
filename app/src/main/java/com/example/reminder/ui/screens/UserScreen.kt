package com.example.reminder.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.reminder.R
import com.example.reminder.mainViewModel

@Composable
fun UserScreen(
    navController : NavController,
) {

    LaunchedEffect(Unit) {
        mainViewModel.getRandomUser()
    }

    androidx.compose.material3.Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar (
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text(stringResource(id = R.string.user_screen_title))
                },
            )
        },
        bottomBar = {
        },
        content = { padding ->
            val lazyListState: LazyListState = rememberLazyListState()

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color.Transparent),
            ) {

                LazyColumn (
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.99f),
                    state = lazyListState,
                ) {
                    itemsIndexed(
                        items = mainViewModel.userListEntity,
                        key = { _, userItem -> userItem.id })
                    { index, _ ->

                        UserItemBlock(
                            index = index,
                            {
                                mainViewModel.userName = mainViewModel.userListEntity[index].name
                                mainViewModel.userEmail = mainViewModel.userListEntity[index].email
                                mainViewModel.userPictureLarge = mainViewModel.userListEntity[index].picture_large
                                mainViewModel.userPictureThumbnail = mainViewModel.userListEntity[index].picture_thumbnail

                                navController.popBackStack()
                            },
                        )

                    }
                }
            }
        }
    )

}


@Composable
@OptIn(ExperimentalFoundationApi::class)
fun UserItemBlock(
    index: Int,
    onSelectUser: ()-> Unit,
    ) {

    val details = mainViewModel.userListEntity[index]

    val name = details.name

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .combinedClickable(

                onClick = {
                    onSelectUser()
                    },
            )

    ) {

        val (photoRef,
            titleRef,
            topDividerRef,
            bottomDividerRef) = createRefs()

        val dividerColor = Color.Transparent

        val dividerThickness = 1.5.dp
        Divider(
            color = dividerColor,
            thickness = dividerThickness,
            modifier = Modifier
                .constrainAs(topDividerRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Divider(
            color = dividerColor,
            thickness = dividerThickness,
            modifier = Modifier
                .constrainAs(bottomDividerRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Box(modifier = Modifier
            .constrainAs(photoRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, margin = 16.dp)
            },
        ) {
            SubcomposeAsyncImage(
                model = details.picture_thumbnail,
                contentDescription = null,


                loading = {
                    CircularProgressIndicator()
                },
            )
        }

        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(photoRef.end, margin = 16.dp )
                    end.linkTo(parent.end, )
                }
        )

    }

}
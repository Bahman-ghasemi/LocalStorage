package com.example.localstorage

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.localstorage.ui.theme.LocalStorageTheme
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val permissionsToRequest =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    private val deniedPermission: MutableList<String> = permissionsToRequest.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalStorageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val context = LocalContext.current
                        val internalList =
                            StorageHelper.InternalStorage.loadImagesToInternal(context)
                                .collectAsState(initial = emptyList()).value
                        val externalList =
                            StorageHelper.ExternalStorage.loadImagesToExternal(context)
                                .collectAsState(initial = emptyList()).value

                        var isPrivate by remember { mutableStateOf(false) }
                        val permissionLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestMultiplePermissions(),
                            onResult = { permissions ->
                                permissions.keys.forEach { key ->
                                    if (permissions[key] == false && !deniedPermission.contains(key))
                                        deniedPermission.add(key)
                                }
                            })
                        val cameraLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.TakePicturePreview(),
                            onResult = {
                                it?.let {
                                    val result = when (isPrivate) {
                                        true -> {
                                            StorageHelper.InternalStorage.saveBitmapToInternal(
                                                context,
                                                UUID.randomUUID().toString(),
                                                it
                                            )
                                        }

                                        false -> {
                                            StorageHelper.ExternalStorage.saveBitmapToExternal(
                                                context,
                                                UUID.randomUUID().toString(),
                                                it
                                            )
                                        }
                                    }

                                    if (!result) {
                                        Toast.makeText(
                                            context,
                                            "cannot save picture",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                        val intentSenderLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = {
                                if (it.resultCode == RESULT_OK)
                                    Toast.makeText(
                                        context,
                                        "delete Successful!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else
                                    Toast.makeText(context, "fail to delete!", Toast.LENGTH_SHORT)
                                        .show()
                            })

                        Column(Modifier.weight(1f)) {
                            Text(text = "LocalStorage")
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(count = 3),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(internalList) { file ->
                                    Image(
                                        bitmap = file.bitmap.asImageBitmap(),
                                        contentDescription = file.name,
                                        modifier = Modifier
                                            .pointerInput(Unit) {
                                                detectTapGestures(onLongPress = {
                                                    StorageHelper.InternalStorage.deleteImageToInternal(
                                                        context,
                                                        file.name
                                                    )
                                                })
                                            }
                                            .padding(4.dp), contentScale = ContentScale.Crop)
                                }
                            }

                            Text(text = "SharedStorage")
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(count = 3),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(externalList) { file ->
                                    AsyncImage(model = file.uri,
                                        contentDescription = file.name,
                                        modifier = Modifier
                                            .pointerInput(Unit) {
                                                detectTapGestures(onLongPress = {
                                                    StorageHelper.ExternalStorage.deleteImageToExternal(
                                                        context,
                                                        file.uri
                                                    )?.let {
                                                            intentSenderLauncher.launch(IntentSenderRequest.Builder(it).build())
                                                    }
                                                })
                                            }
                                            .padding(4.dp),
                                        contentScale = ContentScale.Crop)
                                }
                            }
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                12.dp,
                                alignment = Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                cameraLauncher.launch(null)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                    contentDescription = null
                                )
                            }
                            Switch(checked = isPrivate, onCheckedChange = { isPrivate = it })
                        }
                        LaunchedEffect(key1 = deniedPermission) {
                            permissionLauncher.launch(permissionsToRequest)
                        }
                    }
                }
            }
        }
    }
}
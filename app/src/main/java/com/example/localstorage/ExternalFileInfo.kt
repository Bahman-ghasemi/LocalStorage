package com.example.localstorage

import android.net.Uri

data class ExternalFileInfo (
    val id:Long,
    val name:String,
    val uri:Uri,
    val width:Long,
    val height:Long
)
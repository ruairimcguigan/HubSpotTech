package com.demo.hubspottech.di.repository

import android.content.Context

interface JsonProcessor {

    fun readData(context: Context, fileName: String): Any
    fun writeData(context: Context, inputObject: Any, fileName: String)
}
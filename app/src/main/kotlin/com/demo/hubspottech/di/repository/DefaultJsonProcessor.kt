package com.demo.hubspottech.di.repository

import android.content.Context
import java.io.*

class DefaultJsonProcessor: JsonProcessor {

    private lateinit var fileIn: FileInputStream
    private lateinit var fileOut: FileOutputStream
    private lateinit var objectIn: ObjectInputStream
    private lateinit var objectOut: ObjectOutputStream
    private lateinit var outputObject: Any
    private lateinit var filePath: String

    override fun readData(
        context: Context,
        fileName: String
    ): Any {
        try {
            filePath = context.applicationContext.filesDir.absolutePath
                .toString() + "/" + fileName
            fileIn = FileInputStream(filePath)
            objectIn = ObjectInputStream(fileIn)
            outputObject = objectIn.readObject()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                objectIn.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return outputObject
    }

    override fun writeData(
        context: Context,
        inputObject: Any,
        fileName: String
    ) {
        try {
            filePath = context.applicationContext.filesDir.absolutePath
                .toString() + "/" + fileName
            fileOut = FileOutputStream(filePath)
            objectOut = ObjectOutputStream(fileOut)
            objectOut.writeObject(inputObject)
            fileOut.fd.sync()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                objectOut.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
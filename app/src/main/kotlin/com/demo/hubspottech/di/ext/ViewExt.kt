package com.demo.hubspottech.di.ext

import android.content.Context
import android.view.View
import android.view.View.*
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.visible() : View {
  if (visibility != VISIBLE) {
    visibility = VISIBLE
  }
  return this
}

fun View.invisible() : View {
  if (visibility == VISIBLE) {
    visibility = INVISIBLE
  }
  return this
}

fun View.gone() : View {
  if (visibility != GONE) {
    visibility = GONE
  }
  return this
}

fun View.snack(
  message: String,
  length: Int = Snackbar.LENGTH_LONG) {
  val snack = Snackbar.make(this, message, length)
  snack.show()
}

fun Context.toast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
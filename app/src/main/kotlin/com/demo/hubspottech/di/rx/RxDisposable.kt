package com.demo.hubspottech.di.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class RxDisposable {
    private var compositeDisposable = CompositeDisposable()

    fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}
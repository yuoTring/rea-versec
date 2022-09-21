package com.versec.versecko.util

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w("singlelivedata", "Multiple observers registered but only one will be notified of changes.")

        }

        super.observe(owner, Observer {

            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }

        })
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}
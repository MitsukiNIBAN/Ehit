package com.mitsuki.ehit.core.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mitsuki.ehit.core.model.entity.SearchKey

class MainViewModel @ViewModelInject constructor() : ViewModel() {

    private val mSearchKeyMap: MutableMap<Int, SearchKey> = hashMapOf()

    fun postSearchKey(code: Int, key: SearchKey) {
        mSearchKeyMap[code] = key
    }

    fun removeSearchKey(code: Int) = mSearchKeyMap.remove(code)
}
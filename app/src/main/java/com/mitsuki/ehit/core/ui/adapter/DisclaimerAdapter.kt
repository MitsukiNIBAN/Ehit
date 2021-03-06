package com.mitsuki.ehit.core.ui.adapter

import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.mitsuki.armory.adapter.SingleItemAdapter
import com.mitsuki.armory.extend.view
import com.mitsuki.ehit.R

class DisclaimerAdapter : SingleItemAdapter(true) {

    val onEvent: MutableLiveData<Boolean> = MutableLiveData()

    override val layoutRes: Int = com.mitsuki.ehit.R.layout.item_disclaimer
    override val onViewHolderCreate: ViewHolder.() -> Unit = {
        view<Button>(R.id.disclaimer_yes_btn)?.setOnClickListener { onEvent.postValue(true) }
        view<Button>(R.id.disclaimer_no_btn)?.setOnClickListener { onEvent.postValue(false) }

    }
    override val onViewHolderBind: ViewHolder.() -> Unit = {}

}
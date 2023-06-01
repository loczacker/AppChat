package com.rikkei.training.appchat.ui.tabSticker

import com.rikkei.training.appchat.model.IconModel

interface ClickItemListener {
    fun onItemCLick(iconModel: IconModel, iconName : String)
}
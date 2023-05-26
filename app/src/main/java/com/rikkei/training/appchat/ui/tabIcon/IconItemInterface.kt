package com.rikkei.training.appchat.ui.tabIcon

import com.rikkei.training.appchat.model.IconModel

interface IconItemInterface {
    fun getIcon(iconModel: IconModel, iconName : String)
}
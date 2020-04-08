package com.au.testapp.modules.firstModule.model

import com.au.testapp.modules.firstModule.network.ResponseCode.APPLICATION_NETWORK_RESPONSE_SUCCESS
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class T : Serializable {

    @SerializedName("isApiSuccess")
    @Expose
    var isApiSuccess: Boolean? = false

    @SerializedName("resultCode")
    @Expose
    var resultCode: Int = APPLICATION_NETWORK_RESPONSE_SUCCESS

}
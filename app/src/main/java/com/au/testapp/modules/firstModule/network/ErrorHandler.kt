package com.au.testapp.modules.firstModule.network

import android.content.Context
import android.widget.Toast

class ErrorHandler {
    fun handleError(context: Context?, errorCode: Int) {
        when (errorCode) {
            ResponseCode.APPLICATION_NETWORK_ERROR_TIMEOUT -> Toast.makeText(
                context,
                ErrorMessage.ERROR_MESSAGE_TIME_OUT,
                Toast.LENGTH_SHORT
            ).show()
            ResponseCode.APPLICATION_NETWORK_ERROR_GENERIC -> Toast.makeText(
                context,
                ErrorMessage.ERROR_MESSAGE_GENERIC,
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(context, ErrorMessage.ERROR_MESSAGE_GENERIC, Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        val instance =
            ErrorHandler()

    }
}
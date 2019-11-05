package com.simferopol.api.utils

class ManagerResult<T> (val data:T? = null, val error:String? = null){
    val success = error.isNullOrEmpty()
}
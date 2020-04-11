package com.peter.thelandlord.extensions.stringextensions

fun String.isNonNullStringValid(): Boolean{
    return this.trim() != ""
}

fun String?.isNullableStringValid(): Boolean{
    return !(this == null || this.trim().isEmpty())
}
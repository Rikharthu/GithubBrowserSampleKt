package com.example.uberv.githubbrowsersamplekt.utils

import timber.log.Timber

class DevelopmentTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement)
            = "${super.createStackElementTag(element)}#${element.methodName}:${element.lineNumber}"
}
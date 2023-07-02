package com.gotowin.core.domain

data class MailModel(
    val subject: String,
    val from: String,
    val header: String,
    var message: String,
    var image: String,
    var action: String,
    var actionLink: String,
)

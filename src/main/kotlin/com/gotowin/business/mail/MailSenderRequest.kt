package com.gotowin.business.mail

import com.gotowin.core.domain.MailModel
import com.gotowin.persistance.GotowinUserEntity

interface MailSenderRequest {
    fun getModel(user: GotowinUserEntity): MailModel
}
package com.gotowin.business.mail

import com.gotowin.core.domain.MailModel
import com.gotowin.persistance.GotowinUserEntity

enum class SimpleMailSenderRequest : MailSenderRequest {
    RESET_PASSWORD {
        override fun getModel(user: GotowinUserEntity): MailModel {
            return MailModel(
                subject = "Reset your password",
                header = "Oops",
                message = "It seems that you've forgotten your password.",
                image = "https://cdn.prezna.com/mail/resetPassword.png",
                action = "Reset Password",
                actionLink = getLinkForEmail(user),
                from = "GoToWin Team",
            )
        }
    },

    ACCOUNT_ACTIVATION {
        override fun getModel(user: GotowinUserEntity): MailModel {
            return MailModel(
                subject = "GoToWin Account Activation",
                header = "GoToWin Activation",
                message = "Your Prezna account has been created, please click on the button below to activate it.",
                image = "",
                action = "Activate Your Account",
                actionLink = getLinkForEmail(user),
                from = "GoToWin Team"
            )
        }
    },

    WELCOME {
        override fun getModel(user: GotowinUserEntity): MailModel {
            return MailModel(
                subject = "Welcome to GoToWin!",
                header = "Welcome To GoToWin",
                message = "Thank you for joining our growing community!",
                image = "",
                action = "Go to account",
                actionLink = getLinkForEmail(user),
                from = "GoToWin Team"
            )
        }
    }
    ;

    protected fun getLinkForEmail(user: GotowinUserEntity): String {
        val link = when (this) {
            RESET_PASSWORD -> {
                "/reset/finish/${user.resetKey}"
            }

            ACCOUNT_ACTIVATION -> {
                "/activate/${user.activationKey}"
            }

            WELCOME -> {
                ""
            }
        }

        return "http://localhost:3000$link"
    }
}
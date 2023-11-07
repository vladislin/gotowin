package com.gotowin.business.mail

import com.gotowin.core.domain.MailModel
import com.gotowin.persistance.GotowinUserEntity
import jakarta.mail.internet.InternetAddress
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.util.*


@Service
class MailService(private val javaMailSender: JavaMailSender,
                  private val templateEngine: SpringTemplateEngine) {

    private val logger = LoggerFactory.getLogger(MailService::class.java)
    @Value("\${spring.mail.username}") private val fromAddress: String = ""

    companion object {
        const val USER_KEY = "user"
        const val MODEL_KEY = "model"
        const val DEFAULT_TEMPLATE_NAME = "default"
        const val ADMIN_DEPOSIT_TEMPLATE_NAME = "admin-deposit"
    }
    @Async
    fun sendStandardEmail(user: GotowinUserEntity, model: MailModel) {
        val context = Context(Locale.ENGLISH)
        context.setVariable(USER_KEY, user)
        context.setVariable(MODEL_KEY, model)
        val content = templateEngine.process(DEFAULT_TEMPLATE_NAME, context)
        val subject = model.subject
        val from = InternetAddress(fromAddress, model.from)
        sendMail(from, user.email, subject, content)
    }

    @Async
    fun sendDepositNotificationForAdmin(to: String, subject: String, attributes: Map<String, Any>) {
        val context = Context(Locale.ENGLISH)
        context.setVariables(attributes)
        val content = templateEngine.process(ADMIN_DEPOSIT_TEMPLATE_NAME, context)
        val from = InternetAddress(fromAddress)
        sendMail(from, to, subject, content)
    }
    private fun sendMail(from: InternetAddress, to: String, subject: String, content: String) {
        logger.debug("Send email to '{}' with subject '{}' and content={}", to, subject, content)

        val mimeMessage = javaMailSender.createMimeMessage()

        try {
            val message = MimeMessageHelper(mimeMessage, false, "UTF-8")
            message.setTo(to)
            message.setFrom(from)
            message.setSubject(subject)
            message.setText(content, true)
            javaMailSender.send(mimeMessage)
            logger.debug("Sent email to User '{}'", to)
        } catch (e: Exception) {
            logger.warn("Email could not be sent to user '{}'", to, e)
        }
    }

}
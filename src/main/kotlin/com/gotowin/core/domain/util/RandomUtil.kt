package com.gotowin.core.domain.util

import org.apache.commons.lang3.RandomStringUtils


object RandomUtil {

    fun generateActivationKey(): String = RandomStringUtils.randomNumeric(20)

    fun generateResetKey(): String = RandomStringUtils.randomNumeric(20)

}
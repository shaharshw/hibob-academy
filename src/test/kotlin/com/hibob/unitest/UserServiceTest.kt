package com.hibob.unitest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserServiceTest {

    private val userDaoMock = mock<UserDao>()
    private val notificationServiceMock = mock<NotificationService>()
    private val emailVerificationServiceMock = mock<EmailVerificationService>()

    private val userService = UserService(userDaoMock, notificationServiceMock, emailVerificationServiceMock)

    private val user = User(
        1L,
        "Shahar",
        "shahar@gmail.com",
        "123456",
    )

    @Test
    fun `registerUser should return true when user is registered successfully`() {

        whenever(userDaoMock.findById(user.id)).thenReturn(null)
        whenever(userDaoMock.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationServiceMock.sendVerificationEmail(user.email)).thenReturn(true)

        val result = userService.registerUser(user)
        assertTrue(result)
    }

    @Test
    fun `registerUser should throw IllegalArgumentException when user already exists`() {

        whenever(userDaoMock.findById(user.id)).thenReturn(user)

        assertThrows(IllegalArgumentException::class.java) {
            userService.registerUser(user)
        }
    }

    @Test
    fun `registerUser should throw IllegalStateException when user registration failed`() {

        whenever(userDaoMock.findById(user.id)).thenReturn(null)
        whenever(userDaoMock.save(user.copy(isEmailVerified = false))).thenReturn(false)

        assertThrows(IllegalStateException::class.java) {
            userService.registerUser(user)
        }
    }

    @Test
    fun `registerUser should throw IllegalStateException when failed to send verification email`() {

        whenever(userDaoMock.findById(user.id)).thenReturn(null)
        whenever(userDaoMock.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationServiceMock.sendVerificationEmail(user.email)).thenReturn(false)

        assertThrows(IllegalStateException::class.java) {
            userService.registerUser(user)
        }
    }

    @Test
    fun `verifyUserEmail should send notification email and return true when email is verified successfully`() {

        val token = "123456"

        whenever(userDaoMock.findById(user.id)).thenReturn(user)
        whenever(emailVerificationServiceMock.verifyEmail(user.email, token)).thenReturn(true)
        whenever(userDaoMock.update(user.copy(isEmailVerified = true))).thenReturn(true)
        whenever(notificationServiceMock.sendEmail(user.email, "Welcome ${user.name}!")).thenReturn(true)

        val result = userService.verifyUserEmail(user.id, token)
        verify(notificationServiceMock).sendEmail(user.email, "Welcome ${user.name}!")
        assertTrue(result)
    }

    @Test
    fun `verifyUserEmail should throw IllegalArgumentException when user not found`() {

        whenever(userDaoMock.findById(user.id)).thenReturn(null)

        assertThrows(IllegalArgumentException::class.java) {
            userService.verifyUserEmail(user.id, "123456")
        }
    }

    @Test
    fun `verifyUserEmail should throw IllegalArgumentException when email verification failed`() {

        val token = "123456"

        whenever(userDaoMock.findById(user.id)).thenReturn(user)
        whenever(emailVerificationServiceMock.verifyEmail(user.email, token)).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            userService.verifyUserEmail(user.id, token)
        }
    }

    @Test
    fun `verifyUserEmail should return false when user email is not verified successfully`() {

        val token = "123456"

        whenever(userDaoMock.findById(user.id)).thenReturn(user)
        whenever(emailVerificationServiceMock.verifyEmail(user.email, token)).thenReturn(true)
        whenever(userDaoMock.update(user.copy(isEmailVerified = true))).thenReturn(false)

        val result = userService.verifyUserEmail(user.id, token)

        assertFalse(result)

    }
}
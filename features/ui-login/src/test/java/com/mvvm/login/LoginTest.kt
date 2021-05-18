package com.mvvm.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mvvm.commons.view.Event
import com.mvvm.commons.view.Result
import com.mvvm.data.responses.LoginResponse
import com.mvvm.data.services.ReqresService
import com.mvvm.login.coroutine.TestCoroutineContextProvider
import com.mvvm.login.coroutine.TestCoroutineRule
import com.mvvm.login.util.createJavaClass
import com.mvvm.login.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var loginViewModel: LoginViewModel

    private val coroutineContextProvider = TestCoroutineContextProvider()
    private val loginService = mock<ReqresService>()

    @Before
    fun before() {
        loginViewModel = LoginViewModel(loginService, coroutineContextProvider)
    }

    @Test
    fun testInitialValues() {
        Assert.assertEquals(loginViewModel.input.email.value, "")
        Assert.assertEquals(loginViewModel.input.pass.value, "")
    }

    @Test
    fun `should login`() {
        val result = mock<Observer<Event<Result<LoginResponse>>>>()
        loginViewModel.loginEvent.observeForever(result)

        testCoroutineRule.runBlockingTest {
            val email = "eve.holt@reqres.in"
            val pass = "cityslicka"

            // fake data
            val loginResponse = Response.success(LoginResponse("tokenSuccess"))
            `when`(loginService.login(email = email, password = pass)).thenReturn(loginResponse)

            loginViewModel.email.value = email
            loginViewModel.pass.value = pass
            loginViewModel.login()

            val argument = ArgumentCaptor.forClass(createJavaClass<Event<Result<LoginResponse>>>())

            // get value of parameter in onChange event
            verify(result, times(2)).onChanged(argument.capture())

            // assert result
            Assert.assertEquals(argument.value.peekContent().data?.token, "tokenSuccess")
        }
    }

    @Test
    fun `should Show Missing Password in Response Output`() {
        val result = mock<Observer<Event<Result<LoginResponse>>>>()
        loginViewModel.loginEvent.observeForever(result)

        testCoroutineRule.runBlockingTest {
            val email = "eve.holt@reqres.in"
            val pass = ""

            // fake data
            val jsonResponse = "{" +
                "    \"error\": \"Missing Password\"" +
                "}"
            val errorResponse = Response.error<LoginResponse>(
                400,
                jsonResponse.toResponseBody(
                    "application/json; charset=utf-8".toMediaTypeOrNull()
                )
            )

            `when`(loginService.login(email = email, password = pass)).thenReturn(errorResponse)

            loginViewModel.input.email.value = email
            loginViewModel.input.pass.value = pass
            loginViewModel.login()

            val argument = ArgumentCaptor.forClass(createJavaClass<Event<Result<LoginResponse>>>())

            // get value of parameter in onChange event
            verify(result, times(2)).onChanged(argument.capture())

            Assert.assertEquals(argument.value.peekContent().message, "Missing Password")
        }
    }
}
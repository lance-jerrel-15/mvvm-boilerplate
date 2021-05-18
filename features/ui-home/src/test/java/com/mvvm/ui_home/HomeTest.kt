package com.mvvm.ui_home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.mvvm.data.responses.UsersResponse
import com.mvvm.data.services.ReqresService
import com.mvvm.home.HomeViewModel
import com.mvvm.home.datasource.UsersDataSource
import com.mvvm.home.datasource.UsersDataSourceFactory
import com.mvvm.login.coroutine.TestCoroutineContextProvider
import com.mvvm.login.coroutine.TestCoroutineRule
import com.mvvm.login.util.createJavaClass
import com.mvvm.login.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import retrofit2.Response

@ExperimentalCoroutinesApi
class HomeTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val coroutineContextProvider = TestCoroutineContextProvider()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var usersDataSourceFactory: UsersDataSourceFactory
    private lateinit var usersDataSource: UsersDataSource

    private val loginService = mock<ReqresService>()

    @Before
    fun before() {
        usersDataSourceFactory = UsersDataSourceFactory(coroutineContextProvider, loginService)
        homeViewModel = HomeViewModel(loginService, usersDataSourceFactory)
        usersDataSource = UsersDataSource(loginService, coroutineContextProvider)
    }

    @Test
    fun `should retrieve list`() {
        testCoroutineRule.runBlockingTest {
            //fake data
            val response = Response.success(
                UsersResponse(
                    1, 6, 12, 2,
                    listOf(
                        UsersResponse.Data(id = 1, "lance@com", "lance", "bernardo", "image.jpg"),
                        UsersResponse.Data(id = 2, "jerrel@com", "jerrel", "rizal", "image1.jpg"),
                        UsersResponse.Data(id = 3, "juan@com", "juan", "dela cruz", "image2.jpg")
                    ), UsersResponse.Support("url", "message")
                )
            )

            `when`(loginService.getUsers(1)).thenReturn(response)

            val result = mock<Observer<PagedList<UsersResponse.Data>>>()
            homeViewModel.output.usersEvent.observeForever(result)

            val argument = ArgumentCaptor.forClass(createJavaClass<PagedList<UsersResponse.Data>>())

            verify(result, times(1)).onChanged(argument.capture())

            Assert.assertEquals(argument.value, response.body()!!.data)
        }
    }
}
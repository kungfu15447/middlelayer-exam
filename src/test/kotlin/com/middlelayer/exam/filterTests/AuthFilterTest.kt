package com.middlelayer.exam.filterTests

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.models.auth.ProfileTokenObject
import com.middlelayer.exam.core.models.auth.TokenClaimsObject
import com.middlelayer.exam.web.filters.AuthFilter
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.env.Environment
import org.springframework.mock.env.MockEnvironment
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.mockito.kotlin.any as kAny

@ExtendWith(SpringExtension::class)
@SpringBootTest
class AuthFilterTest {
    @MockBean
    private lateinit var authService: IAuthService

    private lateinit var filter: AuthFilter

    @BeforeEach
    fun init() {
        filter = AuthFilter(authService)
    }

    @Test
    fun `on AuthFilter when request has no or incorrect bearer token in request header returns Unauthorized status result`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        assert(res.status == 401)
    }

    @Test
    fun `on AuthFilter when request has no bearer token in request header returns correct error message`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)


        //Assert
        assert(res.contentAsString == "No token/invalid Bearer token in Authorization header")
    }

    @Test
    fun `on AuthFilter when request has no bearer token in request header then does not call getClaimsFromJWTToken`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        verify(authService, times(0)).getClaimsFromJWTToken(kAny())
    }

    @ParameterizedTest
    @CsvSource(
            "''",
            "B",
            "Bearer",
            "Basic somebasictoken=="
    )
    fun `on AuthFilter when request has incorrect token in request header then returns Unauthorized status result`(token: String) {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", token)
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        assert(res.status == 401)
    }

    @ParameterizedTest
    @CsvSource(
            "''",
            "B",
            "Bearer",
            "Basic somebasictoken=="
    )
    fun `on AuthFilter when request has incorrect token in request header then returns correct error message`(token: String) {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", token)
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        assert(res.contentAsString == "No token/invalid Bearer token in Authorization header")
    }

    @ParameterizedTest
    @CsvSource(
            "''",
            "B",
            "Bearer",
            "Basic somebasictoken=="
    )
    fun `on AuthFilter when request has incorrect token in request header does not call getClaimsFromJWTToken`(token: String) {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", token)
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        verify(authService, times(0)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on AuthFilter success then response has Ok status result`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Bearer someToken")
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        assert(res.status == 200)
    }

    @Test
    fun `on AuthFilter success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(TokenClaimsObject(
                services = emptyList(),
                profileObj = ProfileTokenObject(
                        userId = "UserId",
                        groupId = "GroupId",
                        fullName = "FullName",
                        email = "Email"
                ),
                basicToken = "BasicToken"
        ))
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Bearer someToken")
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on AuthFilter when token could not be parsed then return Unauthorized status result`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenThrow(
                JwtException("Something went wrong with the token")
        )
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Bearer someToken")
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        assert(res.status == 401)
    }

    @Test
    fun `on AuthFilter when token could not be parsed then return correct error message`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenThrow(
                JwtException("Something went wrong with the token")
        )
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Bearer someToken")
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        assert(res.contentAsString == "Invalid token. Unauthorized access")
    }

    @Test
    fun `on AuthFilter when token could not be parsed should still have called getClaimsFromJWTToken once`() {
        //Assign
        `when`(authService.getClaimsFromJWTToken(kAny())).thenThrow(
                JwtException("Something went wrong with the token")
        )
        val req = MockHttpServletRequest()
        req.addHeader("Authorization", "Bearer someToken")
        val res = MockHttpServletResponse()
        val chain = MockFilterChain()

        //Act
        filter.doFilter(req, res, chain)

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

}
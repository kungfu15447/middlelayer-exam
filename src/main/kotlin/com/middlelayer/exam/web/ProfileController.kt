package com.middlelayer.exam.web
import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.models.Profile
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.addHeaderLenient
import okhttp3.internal.http2.Header
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import java.net.URL

@RestController
class ProfileController {

    fun getProfileXsi() {
        val client = OkHttpClient()
        val url = URL("https://scalexi-pp-xsp2.tdc.dk/com.broadsoft.xsi-actions/v2.0/user/PA_4523706117/profile/")

        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization","Basic bTIzNzA2MTE3QHZrMTAzNzQxLmh2b2lwLmRrOjExMTExMQ==")
            .build()
        val response = client.newCall(request).execute()

        val responseBody = response.body!!.string()



        println("Response Body: " + responseBody)



        val mapperAll = ObjectMapper()
        val objData = mapperAll.readTree(responseBody)
        println(objData)


    }

    @GetMapping("tryMethod")
    fun getProfile() {
        getProfileXsi();
    }

    /*

    data class AdditionalDetails(val emailAddress: String?, val mobile: String?, val location: String?, val department: Department?, val impId: String?, val yahooId: String?)

data class Base(val Profile: Profile?)

data class Department(val xsi:nil: String?)

data class Details(val firstName: String?, val lastName: String?, val hiranganaFirstName: String?, val number: String?, val extension: String?, val hiranganaLastName: String?, val groupId: String?, val serviceProvider: ServiceProvider?, val userId: String?)

data class Profile(val xmlns: String?, val registrations: String?, val scheduleList: String?, val timeZoneDisplayName: String?, val portalPasswordChange: String?, val countryCode: String?, val fac: String?, val timeZone: String?, val xmlns:xsi: String?, val details: Details?, val additionalDetails: AdditionalDetails?, val passwordExpiresDays: String?)

data class ServiceProvider(val isEnterprise: String?, val content: String?)
     */
}

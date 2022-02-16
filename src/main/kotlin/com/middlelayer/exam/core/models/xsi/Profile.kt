package com.middlelayer.exam.core.models.xsi
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

data class Profile(
    val details: UserDetails = UserDetails(),
    val additionalDetails: UserAdditionalDetails = UserAdditionalDetails(),
    val passwordExpiresDays: Int? = 0,
    val fac: String? = null,
    val registrations: String? = null,
    val scheduleList: String? = null,
    val portalPasswordChange: String? = null,
    val countryCode: Int? = 0,
    val timeZone: String? = null,
    val timeZoneDisplayName: String? = null
)
data class UserDetails(
    val userId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val hiranganaLastName: String? = null,
    val hiranganaFirstName: String? = null,
    val groupId: String = "",
    val serviceProvider: ServiceProvider? = null,
    val number: String? = null,
    val extension: String? = null,
)

data class UserAdditionalDetails(
    val mobile: String? = null,
    val yahooId: String? = null,
    val pager: String? = null,
    val emailAddress: String? = null,
    val title: String? = null,
    val location: String? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null,
    val country: String? = null,
    val department: String? = null,
    val impId: String? = null,
    val bridgeId: String? = null,
    val roomId: String? = null
)
data class ServiceProvider(@field:JacksonXmlProperty(isAttribute = true) val isEnterprise: Boolean? = false) {
    @field:JacksonXmlText
    val value: String? = null
}
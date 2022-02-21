package com.middlelayer.exam.core.models.xsi


data class Enterprise (
    val startIndex: String? = "",
    val numberOfRecords: String? = "",
    val totalAvailableRecords: String? = "",
    val enterpriseDirectory: List<DirectoryDetails> = emptyList(),
    val xmlns: String? = "",
)

data class EnterpriseDirectory (
    val directoryDetails: List<DirectoryDetails> = emptyList()
)

data class DirectoryDetails (
    val userId: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val hiranganaLastName: String? = "",
    val hiranganaFirstName: String? = "",
    val groupId: String? = "",
    val number: String? = "",
    val extension: String? = "",
    val additionalDetails: AdditionalDetails = AdditionalDetails()
)

data class AdditionalDetails (
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

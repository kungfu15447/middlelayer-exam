package com.middlelayer.exam.core.models.xsi

data class Enterprise (
    val startIndex: String,
    val numberOfRecords: String,
    val totalAvailableRecords: String,
    val enterpriseDirectory: EnterpriseDirectory,
    val xmlns: String
)

data class EnterpriseDirectory (
    val directoryDetails: DirectoryDetails
)

data class DirectoryDetails (
    val userID: String,
    val firstName: String,
    val lastName: String,
    val hiranganaLastName: String,
    val hiranganaFirstName: String,
    val groupID: String,
    val number: String,
    val extension: String,
    val additionalDetails: AdditionalDetails
)

data class AdditionalDetails (
    val mobile: String,
    val yahooID: String,
    val emailAddress: String,
    val location: String,
    val department: String,
    val impID: String
)
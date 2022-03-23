package com.middlelayer.exam.serviceTests
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.ServiceProvider
import com.middlelayer.exam.core.models.xsi.UserAdditionalDetails
import com.middlelayer.exam.core.models.xsi.UserDetails
import com.middlelayer.exam.service.ProfileService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import org.mockito.kotlin.any as kAny

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ProfileServiceTest(@Autowired val profileService: ProfileService) {

    @MockBean
    private lateinit var profileRepo: IProfileRepository

    private fun createXsiProfile(): Profile {
        val profileDetails = UserDetails(
            "userId",
            "firstName",
            "lastName",
            "hiranganaLastName",
            "hiranganaFirstName",
            "groupId",
            ServiceProvider(
                false,
            ),
            "number",
            "extension"
        )
        val additionalProfileDetails = UserAdditionalDetails(
            "mobile",
            "yahooId",
            "pager",
            "emailAddress",
            "title",
            "location",
            "addressLine1",
            "addressLine1",
            "city",
            "state",
            "zip",
            "country",
            "department",
            "impId",
            "bridgeId",
            "roomId",
        )
        return Profile(
            profileDetails,
            additionalProfileDetails,
            100,
            "fac",
            "registrations",
            "scheduleList",
            "portalPasswordChange"
        )
    }

    @Test
    fun `on GetProfile returns Profile`() {
        //Assign
        val profile = createXsiProfile()
        `when`(profileRepo.getProfileXsi(kAny(), kAny())).thenReturn(Mono.just(profile))

        //Act
        val mono = profileService.getProfile("", "")

        //Assert
        mono.subscribe {
            assertThat(it).isEqualTo(profile)
        }
    }


    @Test
    fun `on GetProfile calls getProfileXsi once`() {
        //Assign
        val profile = createXsiProfile()
        `when`(profileRepo.getProfileXsi(kAny(), kAny())).thenReturn(Mono.just(profile))

        //Act
        val mono = profileService.getProfile("", "")

        //Assert
        mono.subscribe {
            verify(profileRepo, times(1)).getProfileXsi(kAny(), kAny())
        }
    }
}
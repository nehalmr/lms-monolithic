package com.library.service;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import com.library.testdata.MemberTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService Tests")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = MemberTestDataBuilder.aMember()
                .withId(1L)
                .withName("John Doe")
                .withEmail("john.doe@example.com")
                .build();
    }

    @Nested
    @DisplayName("Get All Members")
    class GetAllMembersTests {

        @Test
        @DisplayName("Should return all members when members exist")
        void shouldReturnAllMembersWhenMembersExist() {
            // Given
            List<Member> expectedMembers = Arrays.asList(testMember,
                MemberTestDataBuilder.aMember().withId(2L).withName("Jane Doe").build());
            when(memberRepository.findAll()).thenReturn(expectedMembers);

            // When
            List<Member> actualMembers = memberService.getAllMembers();

            // Then
            assertThat(actualMembers).hasSize(2);
            assertThat(actualMembers).containsExactlyElementsOf(expectedMembers);
            verify(memberRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no members exist")
        void shouldReturnEmptyListWhenNoMembersExist() {
            // Given
            when(memberRepository.findAll()).thenReturn(Arrays.asList());

            // When
            List<Member> actualMembers = memberService.getAllMembers();

            // Then
            assertThat(actualMembers).isEmpty();
            verify(memberRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Get Member By ID")
    class GetMemberByIdTests {

        @Test
        @DisplayName("Should return member when member exists")
        void shouldReturnMemberWhenMemberExists() {
            // Given
            when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

            // When
            Optional<Member> result = memberService.getMemberById(1L);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(testMember);
            verify(memberRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when member does not exist")
        void shouldReturnEmptyWhenMemberDoesNotExist() {
            // Given
            when(memberRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<Member> result = memberService.getMemberById(999L);

            // Then
            assertThat(result).isEmpty();
            verify(memberRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Save Member")
    class SaveMemberTests {

        @Test
        @DisplayName("Should save member successfully")
        void shouldSaveMemberSuccessfully() {
            // Given
            when(memberRepository.save(testMember)).thenReturn(testMember);

            // When
            Member result = memberService.saveMember(testMember);

            // Then
            assertThat(result).isEqualTo(testMember);
            verify(memberRepository).save(testMember);
        }
    }

    @Nested
    @DisplayName("Get Member By Email")
    class GetMemberByEmailTests {

        @Test
        @DisplayName("Should return member when email exists")
        void shouldReturnMemberWhenEmailExists() {
            // Given
            String email = "john.doe@example.com";
            when(memberRepository.findByEmail(email)).thenReturn(Optional.of(testMember));

            // When
            Optional<Member> result = memberService.getMemberByEmail(email);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(testMember);
            verify(memberRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should return empty when email does not exist")
        void shouldReturnEmptyWhenEmailDoesNotExist() {
            // Given
            String email = "nonexistent@example.com";
            when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

            // When
            Optional<Member> result = memberService.getMemberByEmail(email);

            // Then
            assertThat(result).isEmpty();
            verify(memberRepository).findByEmail(email);
        }
    }

    @Nested
    @DisplayName("Search Members")
    class SearchMembersTests {

        @Test
        @DisplayName("Should return matching members when name matches")
        void shouldReturnMatchingMembersWhenNameMatches() {
            // Given
            String name = "John";
            List<Member> expectedMembers = Arrays.asList(testMember);
            when(memberRepository.findByNameContainingIgnoreCase(name)).thenReturn(expectedMembers);

            // When
            List<Member> result = memberService.searchMembers(name);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(testMember);
            verify(memberRepository).findByNameContainingIgnoreCase(name);
        }
    }

    @Nested
    @DisplayName("Get Active Members")
    class GetActiveMembersTests {

        @Test
        @DisplayName("Should return only active members")
        void shouldReturnOnlyActiveMembers() {
            // Given
            List<Member> activeMembers = Arrays.asList(testMember);
            when(memberRepository.findByMembershipStatus(Member.MembershipStatus.ACTIVE))
                    .thenReturn(activeMembers);

            // When
            List<Member> result = memberService.getActiveMembers();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(testMember);
            verify(memberRepository).findByMembershipStatus(Member.MembershipStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("Delete Member")
    class DeleteMemberTests {

        @Test
        @DisplayName("Should delete member when member exists")
        void shouldDeleteMemberWhenMemberExists() {
            // When
            memberService.deleteMember(1L);

            // Then
            verify(memberRepository).deleteById(1L);
        }
    }
}

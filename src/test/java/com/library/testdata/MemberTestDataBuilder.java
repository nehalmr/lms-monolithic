package com.library.testdata;

import com.library.entity.Member;
import java.time.LocalDate;

public class MemberTestDataBuilder {
    private Long memberId = 1L;
    private String name = "Test Member";
    private String email = "test@example.com";
    private String phone = "+1-555-0123";
    private String address = "123 Test St";
    private Member.MembershipStatus membershipStatus = Member.MembershipStatus.ACTIVE;
    private LocalDate registrationDate = LocalDate.of(2024, 1, 15);
    
    public static MemberTestDataBuilder aMember() {
        return new MemberTestDataBuilder();
    }
    
    public MemberTestDataBuilder withId(Long memberId) {
        this.memberId = memberId;
        return this;
    }
    
    public MemberTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public MemberTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public MemberTestDataBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public MemberTestDataBuilder withAddress(String address) {
        this.address = address;
        return this;
    }
    
    public MemberTestDataBuilder withStatus(Member.MembershipStatus status) {
        this.membershipStatus = status;
        return this;
    }
    
    public MemberTestDataBuilder withRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }
    
    public MemberTestDataBuilder suspended() {
        this.membershipStatus = Member.MembershipStatus.SUSPENDED;
        return this;
    }
    
    public MemberTestDataBuilder expired() {
        this.membershipStatus = Member.MembershipStatus.EXPIRED;
        return this;
    }
    
    public Member build() {
        return new Member(memberId, name, email, phone, address, membershipStatus, registrationDate);
    }
}

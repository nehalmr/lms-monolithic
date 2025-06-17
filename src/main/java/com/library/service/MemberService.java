package com.library.service;

import com.library.entity.Member;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }
    
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
    
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    
    public List<Member> searchMembers(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Member> getActiveMembers() {
        return memberRepository.findByMembershipStatus(Member.MembershipStatus.ACTIVE);
    }
}

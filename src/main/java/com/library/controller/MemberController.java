package com.library.controller;

import com.library.entity.Member;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Members", description = "Library member management APIs")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    @Operation(
        summary = "Retrieve all members",
        description = "Get a list of all registered library members"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all members"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }
    
    @Operation(
        summary = "Retrieve a member by ID",
        description = "Get a specific member by their unique identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the member"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "400", description = "Invalid member ID supplied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(
        @Parameter(description = "ID of the member to retrieve", required = true, example = "1")
        @PathVariable Long id) {
        Optional<Member> member = memberService.getMemberById(id);
        return member.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(
        summary = "Register a new member",
        description = "Add a new member to the library system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Member registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid member data provided"),
        @ApiResponse(responseCode = "409", description = "Member with same email already exists")
    })
    @PostMapping
    public ResponseEntity<Member> createMember(
        @Parameter(description = "Member object to be registered", required = true)
        @RequestBody Member member) {
        Member createdMember = memberService.saveMember(member);
        return ResponseEntity.status(201).body(createdMember);
    }
    
    @Operation(
        summary = "Update member information",
        description = "Update existing member details by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Member updated successfully"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "400", description = "Invalid member data provided")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
        @Parameter(description = "ID of the member to update", required = true, example = "1")
        @PathVariable Long id,
        @Parameter(description = "Updated member object", required = true)
        @RequestBody Member member) {
        if (!memberService.getMemberById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        member.setMemberId(id);
        return ResponseEntity.ok(memberService.saveMember(member));
    }
    
    @Operation(
        summary = "Delete a member",
        description = "Remove a member from the library system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete member with active borrowings")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
        @Parameter(description = "ID of the member to delete", required = true, example = "1")
        @PathVariable Long id) {
        if (!memberService.getMemberById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Search members",
        description = "Search members by name"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping("/search")
    public List<Member> searchMembers(
        @Parameter(description = "Member name to search for", required = true, example = "John")
        @RequestParam String name) {
        return memberService.searchMembers(name);
    }
    
    @Operation(
        summary = "Get active members",
        description = "Retrieve all members with active membership status"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active members")
    })
    @GetMapping("/active")
    public List<Member> getActiveMembers() {
        return memberService.getActiveMembers();
    }
}

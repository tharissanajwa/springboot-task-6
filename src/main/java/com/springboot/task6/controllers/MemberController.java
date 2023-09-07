package com.springboot.task6.controllers;

import com.springboot.task6.models.ApiResponse;
import com.springboot.task6.models.Member;
import com.springboot.task6.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    // Metode untuk mengambil semua data pegawai dari fungsi yg telah dibuat di service
    @GetMapping("")
    public ResponseEntity getAllMember() {
        List<Member> members = memberService.getAllMember();
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data pegawai berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity getMemberById(@PathVariable("id") Long id) {
        Member members = memberService.getMemberById(id);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        if (members != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Metode untuk membuat pegawai baru dari fungsi yg telah dibuat di service
    @PostMapping("")
    public ResponseEntity insertMember(@RequestBody Member member) {
        Member members = memberService.insertMember(member.getName());
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        if (members != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Metode untuk memperbarui informasi pegawai dari fungsi yg telah dibuat di service
    @PutMapping("/{id}")
    public ResponseEntity updateMember(@PathVariable("id") Long id, @RequestBody Member member) {
        Member members = memberService.updateMember(id, member.getName());
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        if (members != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Metode untuk menghapus pegawai berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity deleteMember(@PathVariable("id") Long id) {
        boolean valid = memberService.deleteMember(id);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), null);
        if (valid) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
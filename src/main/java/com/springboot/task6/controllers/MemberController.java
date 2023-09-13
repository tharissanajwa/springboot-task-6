package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Member;
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

    // Metode untuk mengambil semua data anggota dari fungsi yg telah dibuat di service
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllMember() {
        List<Member> members = memberService.getAllMember();
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data anggota berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMemberById(@PathVariable("id") Long id) {
        Member members = memberService.getMemberById(id);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        HttpStatus httpStatus;

        if (members != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk membuat anggota baru dari fungsi yg telah dibuat di service
    @PostMapping("")
    public ResponseEntity<ApiResponse> insertMember(@RequestBody Member member) {
        Member members = memberService.insertMember(member.getName(), member.getPhone());
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        HttpStatus httpStatus;

        if (members != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return  ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk memperbarui informasi anggota dari fungsi yg telah dibuat di service
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMember(@PathVariable("id") Long id, @RequestBody Member member) {
        Member members = memberService.updateMember(id, member.getName(), member.getPhone());
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), members);
        HttpStatus httpStatus;

        if (members != null)  httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menghapus anggota berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable("id") Long id) {
        boolean isValid = memberService.deleteMember(id);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (isValid) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }
}
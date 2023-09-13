package com.springboot.task6.services;

import com.springboot.task6.model.Member;
import com.springboot.task6.repositories.MemberRepository;
import com.springboot.task6.utilities.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    // Pesan status untuk memberi informasi kepada pengguna
    private String responseMessage;

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar anggota yang belum terhapus melalui repository
    public List<Member> getAllMember() {
        List<Member> result = memberRepository.findAllByDeletedAtIsNullOrderByName();
        if (result.isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data member.";
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return result;
    }

    // Metode untuk mendapatkan data anggota berdasarkan id melalui repository
    public Member getMemberById(Long id) {
        Optional<Member> result = memberRepository.findByIdAndDeletedAtIsNull(id);
        if (!result.isPresent()) {
            responseMessage = "Sorry, id member is not found.";
            return null;
        }
        responseMessage = "Data successfully loaded.";
        return result.get();
    }

    // Metode untuk menambahkan anggota ke dalam data melalui repository
    public Member insertMember(String name, String phone) {
        Member result = null;
        String validateName = Validation.inputTrim(name);
        String validatePhone = Validation.inputTrim(phone);
        if (!inputValidation(validateName).isEmpty()) {
            responseMessage = inputValidation(validateName);
        } else if (!inputPhoneValidation(validatePhone).isEmpty()) {
            responseMessage = inputPhoneValidation(validatePhone);
        } else {
            result = new Member(validateName, validatePhone);
            result.setCreatedAt(new Date());
            memberRepository.save(result);
            responseMessage = "Data successfully added!";
        }
        return result;
    }

    // Metode untuk memperbarui informasi anggota melalui repository
    public Member updateMember(Long id, String name, String phone) {
        Member result = getMemberById(id);
        String validateName = Validation.inputTrim(name);
        String validatePhone = Validation.inputTrim(phone);
        if (result != null) {
            if (!inputValidation(validateName).isEmpty()) {
                responseMessage = inputValidation(validateName);
                return null;
            } else if (!inputPhoneValidation(validatePhone).isEmpty()) {
                responseMessage = inputPhoneValidation(validatePhone);
                return null;
            } else {
                result.setName(validateName);
                result.setPhone(validatePhone);
                result.setUpdatedAt(new Date());
                memberRepository.save(result);
                responseMessage = "Data successfully updated!";
            }
        }
        return result;
    }

    // Metode untuk menghapus data anggota secara soft delete melalui repository
    public boolean deleteMember(Long id) {
        boolean result = false;
        if (getMemberById(id) != null) {
            getMemberById(id).setDeletedAt(new Date());
            Member member = getMemberById(id);
            memberRepository.save(member);
            result = true;
            responseMessage = "Data successfully removed!";
        }
        return result;
    }

    // Metode untuk memvalidasi inputan pengguna
    private String inputValidation(String name) {
        String result = "";
        if (Validation.inputCheck(Validation.inputTrim(name)) == 1) {
            result = "Sorry, member name cannot be blank.";
        }
        if (Validation.inputCheck(Validation.inputTrim(name)) == 2) {
            result = "Sorry, member name can only filled by letters";
        }
        return result;
    }

    private String inputPhoneValidation(String phone) {
        String result = "";
        Optional<Member> memberExist = memberRepository.findByPhoneAndDeletedAtIsNull(Validation.inputTrim(phone));

        if (memberExist.isPresent()) {
            result = "Sorry, member phone already exists.";
        } else if (!phone.trim().matches("^\\d+$") && !(phone.trim().length() >= 10 && phone.trim().length()<=13)) {
            result = "Invalid phone number. Please enter a valid phone number.";
        }
        return result;
    }
}

package com.springboot.task6.services;

import com.springboot.task6.model.Employee;
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

    @Autowired
    private Validation validation;

    // Pesan status untuk memberi informasi kepada pengguna
    private String responseMessage;

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar anggota yang belum terhapus melalui repository
    public List<Member> getAllMember() {
        if (memberRepository.findAllByDeletedAtIsNull().isEmpty()) {
            responseMessage = "Data successfully loaded.";
            seedData();
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return memberRepository.findAllByDeletedAtIsNull();
    }

    // Metode untuk mendapatkan data anggota berdasarkan id melalui repository
    public Member getMemberById(Long id) {
        Optional<Member> result = memberRepository.findByIdAndDeletedAtIsNull(id);
        if (!result.isPresent()) {
            responseMessage = "Sorry, id member is not found.";
            return null;
        } else {
            responseMessage = "Data successfully loaded.";
            return result.get();
        }
    }

    // Metode untuk menambahkan anggota ke dalam data melalui repository
    public Member insertMember(String name) {
        Member result = null;
        if (inputValidation(name) != "") {
            responseMessage = inputValidation(name);
        } else {
            result = new Member(Validation.inputTrim(name));
            result.setCreatedAt(new Date());
            memberRepository.save(result);
            responseMessage = "Data successfully added!";
        }
        return result;
    }

    // Metode untuk memperbarui informasi anggota melalui repository
    public Member updateMember(Long id, String name) {
        Member result = null;
        if (inputValidation(name) != "") {
            responseMessage = inputValidation(name);
        }
        if (getMemberById(id) != null) {
            getMemberById(id).setName(Validation.inputTrim(name));
            result = getMemberById(id);
            result.setUpdatedAt(new Date());
            memberRepository.save(result);
            responseMessage = "Data successfully updated!";
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

    public void seedData() {
        // database seeder
        Member member1 = new Member();
        member1.setName("Sarah Utami");
        member1.setCreatedAt(new Date());
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("Hasan Abdullah");
        member2.setCreatedAt(new Date());
        memberRepository.save(member2);

        Member member3 = new Member();
        member3.setName("Rina Kartika");
        member3.setCreatedAt(new Date());
        memberRepository.save(member3);

        Member member4 = new Member();
        member4.setName("Budi Setiawan");
        member4.setCreatedAt(new Date());
        memberRepository.save(member4);

        Member member5 = new Member();
        member5.setName("Maya Wijaya");
        member5.setCreatedAt(new Date());
        memberRepository.save(member5);
    }
}

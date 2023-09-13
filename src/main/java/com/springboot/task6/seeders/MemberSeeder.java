package com.springboot.task6.seeders;

import com.springboot.task6.model.Member;
import com.springboot.task6.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MemberSeeder {
    @Autowired
    private MemberRepository memberRepository;

    @PostConstruct
    public void seed() {
        // Daftar anggota yang akan disimpan dalam database
        List<Member> members = new ArrayList<>(Arrays.asList(
                new Member("Sarah Utami", "08123456789"),
                new Member("Hasan Abdullah", "08212345678"),
                new Member("Rina Kartika", "08389876543"),
                new Member("Budi Setiawan", "08775555555"),
                new Member("Maya Wijaya", "08561234567")
        ));

        // Cek apakah database sudah berisi data anggota atau tidak
        if (memberRepository.findAllByDeletedAtIsNullOrderByName().isEmpty()) {
            // Jika tidak ada data, maka simpan data anggota ke dalam database
            memberRepository.saveAll(members);
        }
    }
}
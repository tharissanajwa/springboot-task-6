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
        List<Member> members = new ArrayList<>(Arrays.asList(
                new Member("Sarah Utami"),
                new Member("Hasan Abdullah"),
                new Member("Rina Kartika"),
                new Member("Budi Setiawan"),
                new Member("Maya Wijaya")
        ));

        if (memberRepository.findAllByDeletedAtIsNull().isEmpty()) {
            memberRepository.saveAll(members);
        }
    }
}

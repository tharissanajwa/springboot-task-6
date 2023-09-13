package com.springboot.task6.repositories;

import com.springboot.task6.model.Member;
import com.springboot.task6.model.TableOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByDeletedAtIsNullOrderByName();
    Optional<Member> findByIdAndDeletedAtIsNull(Long aLong);
    Optional<Member> findByPhoneAndDeletedAtIsNull(String phone);
}

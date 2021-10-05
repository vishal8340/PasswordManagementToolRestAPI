package com.epam.rd.repository;

import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<Record> findByAccount(Account account);

    Optional<Record> findByUrlAndAccount(String url, Account account);

    List<Record> findByGroupAndAccount(Group existGroup, Account account);

    Optional<Record> findByIdAndAccount(int id, Account account);

    void deleteByIdAndAccount(int id, Account account);
}

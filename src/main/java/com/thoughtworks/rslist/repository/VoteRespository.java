package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRespository extends CrudRepository<VoteEntity,Integer> {
    List<VoteEntity> findAll();
    List<VoteEntity> findAllByLocalDateTimeBetween(LocalDateTime start,LocalDateTime end);

}

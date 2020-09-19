package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface RsEventRespository extends CrudRepository<RsEventEntity,Integer> {
    List<RsEventEntity> findAll();
    @Transactional
    void deleteAllByUserId (Integer userId);
}

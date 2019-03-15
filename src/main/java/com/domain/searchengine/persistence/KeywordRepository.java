package com.domain.searchengine.persistence;

import com.domain.searchengine.persistence.entity.KeywordIndex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KeywordRepository extends CrudRepository<KeywordIndex, UUID> {

    List<KeywordIndex> findByKeyword(String keyword);
}

package com.domain.searchengine.persistence;

import com.domain.searchengine.persistence.entity.HtmlContentIndex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HtmlContentRepository extends CrudRepository<HtmlContentIndex, UUID> {
    HtmlContentIndex findByUrl(String url);
}

package com.domain.searchengine.persistence;

import com.domain.searchengine.persistence.entity.HtmlLinkIndex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HtmlLinkRepository extends CrudRepository<HtmlLinkIndex, UUID> {
}

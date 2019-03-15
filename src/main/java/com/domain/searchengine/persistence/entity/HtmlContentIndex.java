package com.domain.searchengine.persistence.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
public class HtmlContentIndex {

    @Id
    @Column(columnDefinition = "uniqueidentifier", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column
    private String htmlContent;

    @Column
    private LocalDateTime localDateTime = LocalDateTime.now();

}

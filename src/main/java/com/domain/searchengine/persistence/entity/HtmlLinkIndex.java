package com.domain.searchengine.persistence.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "contentId", "linkText"})
public class HtmlLinkIndex {

    @Id
    @Column(columnDefinition = "uniqueidentifier", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column
    private String link;

    @Column
    private String linkText;

    @Column
    @Type(type = "uuid-char")
    private UUID contentId;
}

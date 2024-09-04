package lab.loop.lms.domain.sUrl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lab.loop.lms.global.jpa.BaseEntity;

@Entity
public class SUrl extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String originUrl;

    private String shortUrl;

    private Long redirectCount;

    private Boolean isActive;
}

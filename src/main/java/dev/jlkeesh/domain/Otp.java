package dev.jlkeesh.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Otp extends BaseDomain {
    private String code;
    private Long userId;
    private boolean used;
    private LocalDateTime validTill;
}

package redlib.backend.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class MemberVO {
    private Integer memberId;

    private String phone;

    private String name;

    private BigDecimal balance;
}

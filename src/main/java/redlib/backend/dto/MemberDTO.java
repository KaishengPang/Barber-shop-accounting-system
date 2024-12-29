package redlib.backend.dto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberDTO {
    private Integer memberId;         //id
    private String phone;         //电话号
    private String name;          //名字
    private BigDecimal balance;   //余额
    private String password;
}

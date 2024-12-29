package redlib.backend.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述:会员表的实体类
 * @version
 * @author:  Lenovo
 * @创建时间: 2024-03-12
 */
@Data
public class Members {
    /**
     * 主键：会员ID
     */
    private Integer memberId;

    /**
     * 电话
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 创建日期
     */
    private String password;
    private Date createdAt;
}
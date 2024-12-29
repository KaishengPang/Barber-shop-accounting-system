package redlib.backend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ServiceProjectsVO {
    private Integer id;
    private String projectName;
    private BigDecimal projectFee;
    private Date createdAt;
    private Date updatedAt;
}

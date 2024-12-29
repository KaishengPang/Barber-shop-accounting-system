package redlib.backend.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;


@Data
public class Log {

    private Integer logId;
    private Integer memberId;
    private String changeFlag;
    private Integer serviceProjectId;
    private String projectName;
    private BigDecimal changeAmount;
    private String remark;
    private Date changeDate;
}
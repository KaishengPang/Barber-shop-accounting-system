package redlib.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ServiceProjectsDTO {

    private Integer id;
    private String projectName;
    private BigDecimal projectFee;

}

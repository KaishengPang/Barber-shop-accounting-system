package redlib.backend.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**

 */
@Data
public class ServiceProjects {
    private Integer id;
    private String projectName;
    private BigDecimal projectFee;
    private Date createdAt;
    private Date updatedAt;
}
package redlib.backend.dto.query;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogQueryDTO extends PageQueryDTO {
    private Integer logId;
    private Integer memberId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date changeDate;
    private String orderBy;
}

package redlib.backend.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppointmentQueryDTO extends PageQueryDTO {
    private Integer memberId;
    private String projectName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentEndTime;
    private String status;
}

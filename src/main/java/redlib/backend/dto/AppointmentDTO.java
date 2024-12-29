package redlib.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
@Data
public class AppointmentDTO {
    private Integer appointmentId;
    private Integer memberId;
    private Integer serviceProjectId;
    private String projectName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appointmentEndTime;
    private String status;
    private String remark;
}

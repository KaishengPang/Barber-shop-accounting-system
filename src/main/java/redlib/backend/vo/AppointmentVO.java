package redlib.backend.vo;

import lombok.Data;

import java.util.Date;
@Data
public class AppointmentVO {
    private Integer appointmentId;
    private Integer memberId;
    private Integer serviceProjectId;
    private String projectName;
    private Date appointmentStartTime;
    private Date appointmentEndTime;
    private String status;
    private String remark;
}

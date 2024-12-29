package redlib.backend.model;

import java.util.Date;
import lombok.Data;


@Data
public class Appointment {
    private Integer appointmentId;
    private Integer memberId;
    private Integer serviceProjectId;
    private String projectName;
    private Date appointmentStartTime;
    private Date appointmentEndTime;
    private String status;
    private String remark;
    private Date createdAt;
    private Date updatedAt;
}
package redlib.backend.service;

import redlib.backend.dto.AppointmentDTO;
import redlib.backend.dto.ServiceProjectsDTO;
import redlib.backend.dto.query.AppointmentQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.AppointmentVO;

import java.util.List;

public interface AppointmentService {
    Page<AppointmentVO> listByPage(AppointmentQueryDTO queryDTO);
    void deleteByCodes(List<Integer> ids);
    Integer createAppointment(AppointmentDTO appointmentDTO);

    boolean updateAppointment(AppointmentDTO appointmentDTO);

}

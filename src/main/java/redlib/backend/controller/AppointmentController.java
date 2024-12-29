package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.AppointmentDTO;
import redlib.backend.dto.query.AppointmentQueryDTO;

import redlib.backend.model.Page;
import redlib.backend.service.AppointmentService;
import redlib.backend.vo.AppointmentVO;

import java.util.List;


@RestController
@RequestMapping("/api/appointment")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("listAppointment")
    @Privilege("page")
    public Page<AppointmentVO> listAppointment(@RequestBody AppointmentQueryDTO queryDTO) {
        return appointmentService.listByPage(queryDTO);
    }

    @PostMapping("deleteAppointment")
    @Privilege("delete")
    public void deleteAppointment(@RequestBody List<Integer> ids) {
        appointmentService.deleteByCodes(ids);
    }

    @PostMapping("/create")
    @Privilege("add")
    public Integer create(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.createAppointment(appointmentDTO);
    }

    @PostMapping("/updateAppointment")
    @Privilege("update")
    public boolean updateAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.updateAppointment(appointmentDTO);
    }
}

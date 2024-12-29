package redlib.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.AppointmentMapper;
import redlib.backend.dao.LogMapper;
import redlib.backend.dao.MembersMapper;
import redlib.backend.dao.ServiceProjectsMapper;
import redlib.backend.dto.AppointmentDTO;
import redlib.backend.dto.ServiceProjectsDTO;
import redlib.backend.dto.query.AppointmentQueryDTO;
import redlib.backend.dto.query.MemberQueryDTO;
import redlib.backend.model.Appointment;
import redlib.backend.model.Members;
import redlib.backend.model.Page;
import redlib.backend.model.ServiceProjects;
import redlib.backend.service.AppointmentService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.vo.AppointmentVO;
import redlib.backend.vo.MemberVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentServicelmpl implements AppointmentService {

    @Autowired
    private ServiceProjectsMapper serviceProjectsMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public Page<AppointmentVO> listByPage(AppointmentQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new AppointmentQueryDTO();
        }

        // 对查询条件进行处理，这里示例为电话号码的模糊查询
        queryDTO.setStatus(FormatUtils.makeFuzzySearchTerm(queryDTO.getStatus()));
        // 获取满足查询条件的会员总数
        Integer size = appointmentMapper.count(queryDTO);
        // 创建分页工具类实例
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (size == 0) {
            // 如果没有查询到数据，则返回空的分页数据
            return pageUtils.getNullPage();
        }
        // 分页查询会员信息
        List<Appointment> list = appointmentMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        // 将Members实体列表转换为MemberVO列表
        List<AppointmentVO> voList = new ArrayList<>();
        for (Appointment appointment : list) {
            AppointmentVO vo = new AppointmentVO();
            vo.setAppointmentId(appointment.getAppointmentId());
            vo.setMemberId(appointment.getMemberId());
            vo.setServiceProjectId(appointment.getServiceProjectId());
            vo.setProjectName(appointment.getProjectName());
            vo.setAppointmentStartTime(appointment.getAppointmentStartTime());
            vo.setAppointmentEndTime(appointment.getAppointmentEndTime());
            vo.setStatus(appointment.getStatus());
            vo.setRemark(appointment.getRemark());
            voList.add(vo);
        }
        // 返回包含分页信息和会员VO列表的Page对象
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "会员id列表不能为空");
        appointmentMapper.deleteByCodes(ids);
    }

    @Override
    public Integer createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setMemberId(appointmentDTO.getMemberId());
        appointment.setMemberId(appointmentDTO.getMemberId());
        appointment.setServiceProjectId(appointmentDTO.getServiceProjectId());
        ServiceProjects serviceProject = serviceProjectsMapper.selectByPrimaryKey(appointmentDTO.getServiceProjectId());
        if (serviceProject != null) {
            appointment.setProjectName(serviceProject.getProjectName());
        } else {
            appointment.setProjectName(null);
        }
        appointment.setAppointmentStartTime(appointmentDTO.getAppointmentStartTime());
        appointment.setAppointmentEndTime(appointmentDTO.getAppointmentEndTime());
        appointment.setStatus(appointmentDTO.getStatus());
        appointment.setRemark(appointmentDTO.getRemark());
        int result = appointmentMapper.insertSelective(appointment);
        return result > 0 ? appointment.getAppointmentId() : null;
    }

    @Override
    public boolean updateAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentDTO.getAppointmentId());
        appointment.setMemberId(appointmentDTO.getMemberId());
        appointment.setServiceProjectId(appointmentDTO.getServiceProjectId());
        ServiceProjects serviceProject = serviceProjectsMapper.selectByPrimaryKey(appointmentDTO.getServiceProjectId());
        if (serviceProject != null) {
            appointment.setProjectName(serviceProject.getProjectName());
        } else {
        }
        appointment.setAppointmentStartTime(appointmentDTO.getAppointmentStartTime());
        appointment.setAppointmentEndTime(appointmentDTO.getAppointmentEndTime());
        appointment.setStatus(appointmentDTO.getStatus());
        appointment.setRemark(appointmentDTO.getRemark());
        int result = appointmentMapper.updateByPrimaryKeySelective(appointment);
        return result > 0;
    }

}

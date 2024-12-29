package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.AppointmentQueryDTO;
import redlib.backend.dto.query.MemberQueryDTO;
import redlib.backend.model.Appointment;
import redlib.backend.model.Members;

import java.util.List;

public interface AppointmentMapper {
    int insertSelective(Appointment record);

    int updateByPrimaryKeySelective(Appointment record);

    Integer count(AppointmentQueryDTO queryDTO);

    List<Appointment> list
            (@Param("queryDTO") AppointmentQueryDTO queryDTO,
             @Param("offset") Integer offset,
             @Param("limit") Integer limit
            );

    void deleteByCodes(@Param("codeList") List<Integer> codeList);
}
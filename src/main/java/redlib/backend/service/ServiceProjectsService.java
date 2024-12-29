package redlib.backend.service;

import redlib.backend.dto.ServiceProjectsDTO;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.dto.query.ServiceProjectsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.DepartmentVO;
import redlib.backend.vo.ServiceProjectsVO;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceProjectsService {
    Page<ServiceProjectsVO> listByPage(ServiceProjectsQueryDTO queryDTO);
    Page<ServiceProjectsVO> listByPageForNormal(ServiceProjectsQueryDTO queryDTO);
    Page<ServiceProjectsVO> listByPageForAll(ServiceProjectsQueryDTO queryDTO);
    ServiceProjectsDTO getById(Integer id);
    Integer createServiceProject(ServiceProjectsDTO serviceProjectDto);

    boolean updateServiceProject(ServiceProjectsDTO serviceProjectDto);

    void deleteByCodes(List<Integer> ids);

}

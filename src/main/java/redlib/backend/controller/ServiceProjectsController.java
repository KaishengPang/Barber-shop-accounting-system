package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.DepartmentDTO;
import redlib.backend.dto.ServiceProjectsDTO;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.dto.query.ServiceProjectsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.ServiceProjectsService;
import redlib.backend.vo.DepartmentVO;
import redlib.backend.vo.ServiceProjectsVO;

import java.util.List;

/**
 * 服务项目管理后端服务模块
 */
@RestController
@RequestMapping("/api/serviceProjects")
public class ServiceProjectsController {

    @Autowired
    private ServiceProjectsService serviceProjectsService;

    @PostMapping("listServiceProjects")
    @Privilege("page")
    public Page<ServiceProjectsVO> listServiceProjects(@RequestBody ServiceProjectsQueryDTO queryDTO) {
        return serviceProjectsService.listByPage(queryDTO);
    }

    @PostMapping("listServiceProjectsForNormal")
    @Privilege("page")
    public Page<ServiceProjectsVO> listServiceProjectsForNormal(@RequestBody ServiceProjectsQueryDTO queryDTO) {
        return serviceProjectsService.listByPageForNormal(queryDTO);
    }
    @PostMapping("listServiceProjectsForAll")
    @Privilege("page")
    public Page<ServiceProjectsVO> listServiceProjectsForAll(@RequestBody ServiceProjectsQueryDTO queryDTO) {
        return serviceProjectsService.listByPageForAll(queryDTO);
    }

    @GetMapping("getServiceProjects")
    @Privilege("update")
    public ServiceProjectsDTO getServiceProjects(Integer id) {
        return serviceProjectsService.getById(id);
    }

    @PostMapping("deleteServiceProjects")
    @Privilege("delete")
    public void deleteServiceProjects(@RequestBody List<Integer> ids) {
        serviceProjectsService.deleteByCodes(ids);
    }

    /**
     * 创建服务项目
     */
    @PostMapping("/create")
    @Privilege("add")
    public Integer createServiceProject(@RequestBody ServiceProjectsDTO serviceProjectDto) {
        return serviceProjectsService.createServiceProject(serviceProjectDto);
    }

    /**
     * 更新服务项目
     */
    @PostMapping("/update")
    @Privilege("update")
    public boolean updateServiceProject(@RequestBody ServiceProjectsDTO serviceProjectDto) {
        return serviceProjectsService.updateServiceProject(serviceProjectDto);
    }


}


package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ServiceProjectsMapper;
import redlib.backend.dto.ServiceProjectsDTO;
import redlib.backend.dto.query.ServiceProjectsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.ServiceProjects;
import redlib.backend.service.ServiceProjectsService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.vo.ServiceProjectsVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ServiceProjectsServiceImpl implements ServiceProjectsService {

    @Autowired
    private ServiceProjectsMapper serviceProjectsMapper;


    @Override
    public Page<ServiceProjectsVO> listByPage(ServiceProjectsQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ServiceProjectsQueryDTO();
        }

        queryDTO.setProjectName(FormatUtils.makeFuzzySearchTerm(queryDTO.getProjectName()));
        Integer size = serviceProjectsMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return pageUtils.getNullPage();
        }

        List<ServiceProjects> list = serviceProjectsMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());


        List<ServiceProjectsVO> voList = new ArrayList<>();
        for (ServiceProjects project : list) {
            ServiceProjectsVO vo = new ServiceProjectsVO();
            BeanUtils.copyProperties(project, vo);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Page<ServiceProjectsVO> listByPageForNormal(ServiceProjectsQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ServiceProjectsQueryDTO();
        }

        queryDTO.setProjectName(FormatUtils.makeFuzzySearchTerm(queryDTO.getProjectName()));
        Integer size = serviceProjectsMapper.countForNormal(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return pageUtils.getNullPage();
        }

        List<ServiceProjects> list = serviceProjectsMapper.listForNormal(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());


        List<ServiceProjectsVO> voList = new ArrayList<>();
        for (ServiceProjects project : list) {
            ServiceProjectsVO vo = new ServiceProjectsVO();
            BeanUtils.copyProperties(project, vo);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Page<ServiceProjectsVO> listByPageForAll(ServiceProjectsQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ServiceProjectsQueryDTO();
        }

        queryDTO.setProjectName(FormatUtils.makeFuzzySearchTerm(queryDTO.getProjectName()));
        Integer size = serviceProjectsMapper.countForAll(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            return pageUtils.getNullPage();
        }

        List<ServiceProjects> list = serviceProjectsMapper.listForAll(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());


        List<ServiceProjectsVO> voList = new ArrayList<>();
        for (ServiceProjects project : list) {
            ServiceProjectsVO vo = new ServiceProjectsVO();
            BeanUtils.copyProperties(project, vo);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public ServiceProjectsDTO getById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "部门id不能为空");
        ServiceProjects serviceprojects = serviceProjectsMapper.selectByPrimaryKey(id);
        Assert.notNull(serviceprojects, "id不存在");
        ServiceProjectsDTO dto = new ServiceProjectsDTO();
        BeanUtils.copyProperties(serviceprojects, dto);
        return dto;
    }

    @Override
    public Integer createServiceProject(ServiceProjectsDTO serviceProjectDto) {
        ServiceProjects serviceProject = new ServiceProjects();
        serviceProject.setProjectName(serviceProjectDto.getProjectName());
        serviceProject.setProjectFee(serviceProjectDto.getProjectFee());
        serviceProject.setCreatedAt(new Date());
        serviceProject.setUpdatedAt(new Date());
        int result = serviceProjectsMapper.insertSelective(serviceProject);
        return result > 0 ? serviceProject.getId() : null;
    }

    @Override
    public boolean updateServiceProject(ServiceProjectsDTO serviceProjectDto) {
        ServiceProjects serviceProject = new ServiceProjects();
        serviceProject.setId(serviceProjectDto.getId());
        serviceProject.setProjectName(serviceProjectDto.getProjectName());
        serviceProject.setProjectFee(serviceProjectDto.getProjectFee());
        serviceProject.setUpdatedAt(new Date());
        int result = serviceProjectsMapper.updateByPrimaryKeySelective(serviceProject);
        return result > 0;
    }

    /**
     * 根据编码列表，批量删除服务项目
     *
     * @param ids 编码列表
     */
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "服务项目id列表不能为空");
        serviceProjectsMapper.deleteByCodes(ids);
    }



}

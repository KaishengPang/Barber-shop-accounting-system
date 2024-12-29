package redlib.backend.service.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import redlib.backend.dao.LogMapper;
import redlib.backend.dao.MembersMapper;
import redlib.backend.dao.ServiceProjectsMapper;
import redlib.backend.dto.MemberDTO;
import redlib.backend.dto.query.MemberQueryDTO;
import redlib.backend.model.*;
import redlib.backend.model.Members;
import redlib.backend.service.MemberService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.vo.MemberVO;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static redlib.backend.utils.SecurityUtils.hashPassword;

@Service
public class MemberServicelmpl implements MemberService {
    @Autowired
    private MembersMapper membersMapper;
    @Autowired
    private ServiceProjectsMapper serviceProjectsMapper;
    @Autowired
    private LogMapper logMapper;


    /**
     * 分页获取部门信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的部门数据列表
     */
    @Override
    public Page<MemberVO> listByPage(MemberQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new MemberQueryDTO();
        }
        // 对查询条件进行处理，这里示例为电话号码的模糊查询
        queryDTO.setPhone(FormatUtils.makeFuzzySearchTerm(queryDTO.getPhone()));
        queryDTO.setName(FormatUtils.makeFuzzySearchTerm(queryDTO.getName()));
        System.out.println(queryDTO.getName());
        System.out.println(queryDTO.getPhone());
        // 获取满足查询条件的会员总数
        Integer size = membersMapper.count(queryDTO);
        // 创建分页工具类实例
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (size == 0) {
            // 如果没有查询到数据，则返回空的分页数据
            return pageUtils.getNullPage();
        }
        // 分页查询会员信息
        List<Members> list = membersMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        // 将Members实体列表转换为MemberVO列表
        List<MemberVO> voList = new ArrayList<>();
        for (Members member : list) {
            MemberVO vo = new MemberVO();
            vo.setMemberId(member.getMemberId());
            vo.setPhone(member.getPhone());
            vo.setName(member.getName());
            vo.setBalance(member.getBalance());
            voList.add(vo);
        }
        // 返回包含分页信息和会员VO列表的Page对象
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Integer registerMember(MemberDTO memberDto) {
        Members member = new Members();
        member.setPhone(memberDto.getPhone());
        member.setName(memberDto.getName());
        member.setBalance(memberDto.getBalance());
        member.setCreatedAt(new Date());
        try {
            // 将密码转化为哈希值后存储
            member.setPassword(hashPassword(memberDto.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // 处理哈希算法不存在的异常，可能返回错误码或错误信息
            return null;
        }
        int result = membersMapper.insertSelective(member);
        if (result > 0) {
            // 记录开通会员日志
            Log log = new Log();
            log.setMemberId(member.getMemberId());
            log.setChangeFlag("开通会员");
            log.setChangeAmount(memberDto.getBalance()); // 初始余额
            log.setRemark("会员开通，初始充值");
            log.setChangeDate(new Date());
            logMapper.insertSelective(log);
        }
        return result > 0 ? member.getMemberId() : null;
    }

    @Override
    public BigDecimal recharge(Integer memberId, BigDecimal amount) {
        Members member = membersMapper.selectByPrimaryKey(memberId);
        if (member != null) {
            member.setBalance(member.getBalance().add(amount));
            membersMapper.updateByPrimaryKeySelective(member);
            // 记录充值日志
            Log log = new Log();
            log.setMemberId(memberId);
            log.setChangeFlag("充值");
            log.setChangeAmount(amount); // 充值金额
            log.setRemark("会员充值");
            log.setChangeDate(new Date());
            logMapper.insertSelective(log);
            return member.getBalance();
        }
        return null;
    }

    @Override
    public BigDecimal rechargeForBonus(Integer memberId, BigDecimal amount) {
        Members member = membersMapper.selectByPrimaryKey(memberId);
        if (member != null) {
            member.setBalance(member.getBalance().add(amount));
            membersMapper.updateByPrimaryKeySelective(member);
            // 记录充值日志
            Log log = new Log();
            log.setMemberId(memberId);
            log.setChangeFlag("赠送充值");
            log.setChangeAmount(amount); // 充值金额
            log.setRemark("赠送会员充值");
            log.setChangeDate(new Date());
            logMapper.insertSelective(log);
            return member.getBalance();
        }
        return null;
    }

    @Override
    @Transactional
    public BigDecimal consume(Integer memberId, Integer serviceProjectId, String password) {
        Members member = membersMapper.selectByPrimaryKey(memberId);
        ServiceProjects serviceProject = serviceProjectsMapper.selectByPrimaryKey(serviceProjectId);
        if (member != null && serviceProject != null) {
            try {
                // 验证密码
                String hashedPassword = hashPassword(password);
                if (!hashedPassword.equals(member.getPassword())) {
                    throw new RuntimeException("密码错误");
                }
            } catch (NoSuchAlgorithmException e) {
                // 处理NoSuchAlgorithmException异常
                throw new IllegalStateException("密码哈希算法配置错误", e);
            }
            // 执行消费逻辑
            BigDecimal newBalance = member.getBalance().subtract(serviceProject.getProjectFee());
            if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                member.setBalance(newBalance);
                membersMapper.updateByPrimaryKeySelective(member);
                // 记录消费日志
                Log log = new Log();
                log.setMemberId(memberId);
                log.setChangeFlag("消费");
                log.setServiceProjectId(serviceProjectId);
                log.setProjectName(serviceProject.getProjectName());
                log.setChangeAmount(serviceProject.getProjectFee().negate()); // 消费为负值
                log.setRemark("会员消费");
                log.setChangeDate(new Date());
                logMapper.insertSelective(log);
                return newBalance;
            } else {
                throw new RuntimeException("余额不足，请您充值后再消费");
            }

        }
        return null;
    }

    /**
     * 根据编码列表，批量删除部门
     *
     * @param ids 编码列表
     */
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "会员id列表不能为空");
        membersMapper.deleteByCodes(ids);
    }

    /**
     * 更新部门数据
     *
     * @param memberDTO 部门输入对象
     * @return 部门编码
     */
    @Override
    public Integer updateMembers(MemberDTO memberDTO) {
        Assert.notNull(memberDTO.getMemberId(), "会员id不能为空");
        Members members = membersMapper.selectByPrimaryKey(memberDTO.getMemberId());
        Assert.notNull(members, "没有找到会员，Id为：" + memberDTO.getMemberId());
        // 先复制属性，但不包括password字段
        BeanUtils.copyProperties(memberDTO, members, "password");
        // 单独处理password字段，只有当DTO中的password不为空时才更新
        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
            try {
                // 假设hashPassword是之前定义的方法，用于生成密码的哈希值
                String hashedPassword = hashPassword(memberDTO.getPassword());
                members.setPassword(hashedPassword);
            } catch (NoSuchAlgorithmException e) {
                // 处理NoSuchAlgorithmException，可以选择记录日志或抛出运行时异常
                throw new IllegalStateException("密码哈希算法配置错误", e);
            }
        }
        membersMapper.updateByPrimaryKey(members);
        return members.getMemberId();
    }


    @Override
    public MemberDTO getById(Integer memberId) {
        Assert.notNull(memberId, "请提供id");
        Assert.notNull(memberId, "会员id不能为空");
        Members member = membersMapper.selectByPrimaryKey(memberId);
        Assert.notNull(member, "id不存在");
        MemberDTO dto = new MemberDTO();
        BeanUtils.copyProperties(member, dto);
        return dto;
    }

    @Override
    public BigDecimal cancelLastFinancialActivity() {
        // 直接查找最后一次财务变动记录，无需指定 memberId
        Log lastFinancialChange = logMapper.findLastFinancialChange();
        if (lastFinancialChange == null) {
            throw new RuntimeException("没有找到最近的财务变动记录");
        }
        Integer memberId = lastFinancialChange.getMemberId();
        Members member = membersMapper.selectByPrimaryKey(memberId);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }

        // 根据财务变动类型更新余额
        switch (lastFinancialChange.getChangeFlag()) {
            case "非会员消费":
                break;
            case "充值":
                member.setBalance(member.getBalance().subtract(lastFinancialChange.getChangeAmount()));
                break;
            case "赠送充值":
                member.setBalance(member.getBalance().subtract(lastFinancialChange.getChangeAmount()));
                break;
            case "消费":
                // 如果是消费，逻辑上应该是增加余额，因为是撤销消费
                member.setBalance(member.getBalance().subtract(lastFinancialChange.getChangeAmount()));
                //至于为什么用subtract是因为消费会导致金额为负！
                break;
            default:
                throw new RuntimeException("未知的财务变动类型：" + lastFinancialChange.getChangeFlag());
        }
        // 更新会员余额
        membersMapper.updateByPrimaryKeySelective(member);
        // 记录撤销操作日志
//        Log cancelLog = new Log();
//        cancelLog.setMemberId(memberId);
//        cancelLog.setChangeFlag("撤销" + lastFinancialChange.getChangeFlag());
//        cancelLog.setProjectName(lastFinancialChange.getProjectName());
//        cancelLog.setChangeAmount(lastFinancialChange.getChangeAmount().negate()); // 撤销操作，金额取反
//        cancelLog.setRemark("撤销最近一次的" + lastFinancialChange.getChangeFlag());
//        cancelLog.setChangeDate(new Date());
//        logMapper.insertSelective(cancelLog);
        logMapper.deleteLogById(lastFinancialChange.getLogId());
        return member.getBalance();
    }

    @Override
    public BigDecimal cancelById(Integer logId) {
        Log targetLog = logMapper.findLogById(logId);
        if (targetLog == null) {
            throw new RuntimeException("没有找到指定ID的财务变动记录");
        }
        Integer memberId = targetLog.getMemberId();
        Members member = membersMapper.selectByPrimaryKey(memberId);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }

        // 根据财务变动类型更新余额
        switch (targetLog.getChangeFlag()) {
            case "充值":
                member.setBalance(member.getBalance().subtract(targetLog.getChangeAmount()));
                break;
            case "赠送充值":
                member.setBalance(member.getBalance().subtract(targetLog.getChangeAmount()));
                break;
            case "消费":
                member.setBalance(member.getBalance().subtract(targetLog.getChangeAmount()));
                break;
            case "非会员消费":
                break;
            default:
                throw new RuntimeException("不可撤销");
        }
        // 更新会员余额
        membersMapper.updateByPrimaryKeySelective(member);
        // 删除该财务变动日志
        logMapper.deleteLogById(logId);
        return member.getBalance();
    }


    public BigDecimal consumeForPublic(Integer serviceProjectId) {
        ServiceProjects serviceProject = serviceProjectsMapper.selectByPrimaryKey(serviceProjectId);

        if (serviceProject != null) {
            // 记录消费日志
            Log log = new Log();
            log.setMemberId(1); // 大众消费，会员ID为1
            log.setChangeFlag("非会员消费");
            log.setServiceProjectId(serviceProjectId);
            log.setProjectName(serviceProject.getProjectName());
            log.setChangeAmount(serviceProject.getProjectFee().negate()); // 消费为负值
            log.setRemark("非会员消费");
            log.setChangeDate(new Date());
            logMapper.insertSelective(log);

            return null; // 对于大众消费，可能没有余额的概念
        }
        return null;
    }
}

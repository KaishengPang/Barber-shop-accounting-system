package redlib.backend.service;

import redlib.backend.dto.DepartmentDTO;
import redlib.backend.dto.MemberDTO;
import redlib.backend.dto.query.MemberQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.MemberVO;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MemberService {
    Page<MemberVO> listByPage(MemberQueryDTO queryDTO);
    Integer registerMember(MemberDTO memberDto);

    BigDecimal recharge(Integer memberId, BigDecimal amount);

    BigDecimal rechargeForBonus(Integer memberId, BigDecimal amount);

    BigDecimal consume(Integer memberId, Integer serviceProjectId, String password);


    /**
     * 根据编码列表，批量删除会员
     *
     * @param ids 编码列表
     */
    void deleteByCodes(List<Integer> ids);
    /**
     * 更新部门数据
     *
     * @param memberDTO 部门输入对象
     * @return 部门编码
     */
    Integer updateMembers(MemberDTO memberDTO);
    MemberDTO getById(Integer memberId);

    public BigDecimal cancelLastFinancialActivity() ;
    BigDecimal cancelById(Integer logId);

    public BigDecimal consumeForPublic(Integer serviceProjectId);
}


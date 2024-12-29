package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.DepartmentDTO;
import redlib.backend.dto.MemberDTO;
import redlib.backend.dto.query.MemberQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.MemberService;
import redlib.backend.vo.MemberVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员管理后端服务模块
 */
@RestController
@RequestMapping("/api/member")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("listMembers")
    @Privilege("page")
    public Page<MemberVO> listMembers(@RequestBody MemberQueryDTO queryDTO) {
        return memberService.listByPage(queryDTO);
    }
    /**
     * 开通会员
     */
    @PostMapping("/register")
    @Privilege("add")
    public Integer registerMember(@RequestBody MemberDTO memberDto) {
        return memberService.registerMember(memberDto);
    }

    /**
     * 会员充值
     */
    @PostMapping("/recharge")
    @Privilege("update")
    public BigDecimal recharge(@RequestParam("memberId") Integer memberId, @RequestParam("amount") BigDecimal amount) {
        return memberService.recharge(memberId, amount);
    }

    /**
     * 会员充值(赠送金额)
     */
    @PostMapping("/rechargeForBonus")
    @Privilege("update")
    public BigDecimal rechargeForBonus(@RequestParam("memberId") Integer memberId, @RequestParam("amount") BigDecimal amount) {
        return memberService.rechargeForBonus(memberId, amount);
    }

    /**
     * 会员消费
     */
    @PostMapping("/consume")
    @Privilege("update")
    public BigDecimal consume(@RequestParam("memberId") Integer memberId, @RequestParam("serviceProjectId") Integer serviceProjectId,@RequestParam("password") String password) {
        return memberService.consume(memberId, serviceProjectId, password);
    }

    @PostMapping("deleteMembers")
    @Privilege("delete")
    public void deleteMembers(@RequestBody List<Integer> ids) {
        memberService.deleteByCodes(ids);
    }

    @PostMapping("updateMembers")
    @Privilege("update")
    public Integer updateMembers(@RequestBody MemberDTO memberDTO) {
        return memberService.updateMembers(memberDTO);
    }
    @GetMapping("getMember")
    @Privilege("update")
    public MemberDTO getMember(Integer memberId) {
        return memberService.getById(memberId);
    }

    @PostMapping("/cancelById")
    @Privilege("update")
    public BigDecimal cancelById(@RequestParam("logId") Integer logId) {
        return memberService.cancelById(logId);
    }

    @PostMapping("/cancelLastFinancialChange")
    @Privilege("update")
    public BigDecimal cancelLastFinancialChange() {
        return memberService.cancelLastFinancialActivity();
    }
    /**
     * 大众会员消费
     */
    @GetMapping ("/consumeForPublic")
    @Privilege("update")
    public BigDecimal consumeForPublic(Integer serviceProjectId) {
        return memberService.consumeForPublic(serviceProjectId);
    }

}

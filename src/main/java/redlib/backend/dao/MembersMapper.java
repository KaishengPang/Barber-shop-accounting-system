package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.MemberQueryDTO;
import redlib.backend.model.Members;
import redlib.backend.model.ServiceProjects;
import java.math.BigDecimal;
import java.util.List;
public interface MembersMapper {

    int insertSelective(Members record);//

    Members selectByPrimaryKey(Integer memberId);//

    int updateByPrimaryKeySelective(Members record);//

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(MemberQueryDTO queryDTO);

    /**
     * 根据查询条件获取会员列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 部门列表
     */
    List<Members> list
    (@Param("queryDTO") MemberQueryDTO queryDTO,
     @Param("offset") Integer offset,
     @Param("limit") Integer limit
    );

    int updateByPrimaryKey(Members record);//
    /**
     * 根据代码列表批量删除会员
     *
     * @param codeList id列表
     */
    void deleteByCodes(@Param("codeList") List<Integer> codeList);


}

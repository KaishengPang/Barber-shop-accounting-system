package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.model.AdminPriv;

import java.util.List;

public interface AdminPrivMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminPriv record);

    AdminPriv selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AdminPriv record);

    List<AdminPriv> list(Integer id);

    void deleteByAdminIds(@Param("ids")List<Integer> ids);
}
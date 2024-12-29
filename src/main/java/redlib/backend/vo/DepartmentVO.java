package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/3 10:22
 */
@Data
public class DepartmentVO {
    private Integer id;
    private String departmentName;
    private String contact;
    private String contactPhone;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Integer createdBy;
    private String createdByDesc;
}

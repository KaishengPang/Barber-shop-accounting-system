package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentQueryDTO extends PageQueryDTO {
    /**
     * 部门名称，模糊匹配
     */
    private String departmentName;
}

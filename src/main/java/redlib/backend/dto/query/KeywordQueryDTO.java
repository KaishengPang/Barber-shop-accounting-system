package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KeywordQueryDTO extends PageQueryDTO {
    private String keyword;
    private String orderBy;
}

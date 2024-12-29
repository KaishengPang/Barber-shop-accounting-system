package redlib.backend.dto.query;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberQueryDTO extends PageQueryDTO {
    private String phone;
    private String name;
}

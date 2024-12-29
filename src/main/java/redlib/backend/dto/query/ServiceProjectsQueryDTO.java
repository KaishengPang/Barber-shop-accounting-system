package redlib.backend.dto.query;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceProjectsQueryDTO extends PageQueryDTO {
    private String projectName;
}

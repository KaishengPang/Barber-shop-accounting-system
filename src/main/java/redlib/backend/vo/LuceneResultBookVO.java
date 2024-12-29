package redlib.backend.vo;

import lombok.Data;

import java.util.List;

@Data
public class LuceneResultBookVO {
    private Integer bookId;
    private String title;
    private String description;

    private Double score;
    private List<String> highlights;
}

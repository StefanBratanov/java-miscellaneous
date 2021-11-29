package interview_questions;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Builder
@Data
public class WordGroup {

    @Singular
    private List<String> words;
}

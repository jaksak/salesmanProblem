package pl.longhorn.komwojazMain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputData {
    @JsonProperty
    private Solution solution;
    @JsonProperty
    private List<TargetPoint> targetPoints;

    public InputData(Solution solution, List<TargetPoint> targetPoints) {
        this.solution = solution;
        this.targetPoints = targetPoints;
    }
}

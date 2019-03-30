package pl.longhorn.komwojazWorker;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputData {
    private Solution solution;
    private List<TargetPoint> targetPoints;

    public InputData(Solution solution, List<TargetPoint> targetPoints) {
        this.solution = solution;
        this.targetPoints = targetPoints;
    }
}

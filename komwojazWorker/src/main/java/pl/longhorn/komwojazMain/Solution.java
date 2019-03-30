package pl.longhorn.komwojazMain;

import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Solution {
    private List<TargetPoint> order;
    private int quality = -1;

    public static Solution random(List<TargetPoint> targetPointsOriginal) {
        List<TargetPoint> targetPoints = new LinkedList<>(targetPointsOriginal);
        TargetPoint initial = targetPoints.get(0);
        targetPoints.remove(0);

        List<TargetPoint> order = new LinkedList<>();

        order.add(initial);

        Random random = new Random();
        while (!targetPoints.isEmpty()) {
            int randomIndex = targetPoints.size() > 1 ? random.nextInt(targetPoints.size()) : 0;
            order.add(targetPoints.get(randomIndex));
            targetPoints.remove(randomIndex);
        }

        order.add(initial);

        return new Solution(order);
    }

    public Solution(List<TargetPoint> order) {
        this.order = order;
    }
}

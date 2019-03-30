package pl.longhorn.komwojazWorker;

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

    public Solution crossWith(Solution solution){
        int solutionSize = order.size();
        List<TargetPoint> newOrder = new LinkedList<>();
        for(int i = 0; i < solutionSize; i++){
            newOrder.add(i % 2 == 0 ? order.get(i) : solution.order.get(i));
        }
        return new Solution(newOrder);
    }

    public Solution mutate(){
        Random random = new Random();
        int firstRandomIndex = random.nextInt(order.size() - 2) + 1;
        int secondRandomIndex = random.nextInt(order.size() - 2) + 1;

        TargetPoint first = order.get(firstRandomIndex);
        TargetPoint second = order.get(secondRandomIndex);

        List<TargetPoint> newOrder = new LinkedList<>(order);

        newOrder.set(firstRandomIndex, second);
        newOrder.set(secondRandomIndex, first);

        return new Solution(newOrder);
    }

    public Solution(List<TargetPoint> order) {
        this.order = order;
    }
}

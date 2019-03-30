package pl.longhorn.komwojazMain;

import lombok.*;

import java.util.Random;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TargetPoint {
    private int x;
    private int y;

    public static TargetPoint generate(){
        Random random = new Random();
        return new TargetPoint(random.nextInt(100), random.nextInt(100));
    }

    public int getDistanceBeetween(TargetPoint other){
        double ac = Math.abs(other.y - y);
        double cb = Math.abs(other.x - x);

        return (int) Math.hypot(ac, cb);
    }
}

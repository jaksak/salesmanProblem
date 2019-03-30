package pl.longhorn.komwojazWorker;

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

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}

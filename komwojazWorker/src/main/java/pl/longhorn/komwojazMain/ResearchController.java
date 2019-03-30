package pl.longhorn.komwojazMain;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/research")
public class ResearchController {

    @PostMapping
    public InputData research(@RequestBody InputData inputData){
        Solution solution = inputData.getSolution();

        TargetPoint lastPoint = inputData.getSolution().getOrder().get(0);
        int realQuality = 0;
        for(int i = 1; i < solution.getOrder().size(); i++){
            TargetPoint actualPoint = solution.getOrder().get(i);
            realQuality += lastPoint.getDistanceBeetween(actualPoint);
            lastPoint = actualPoint;
        }
        solution.setQuality(realQuality);
        inputData.setSolution(solution);
        return inputData;
    }
}

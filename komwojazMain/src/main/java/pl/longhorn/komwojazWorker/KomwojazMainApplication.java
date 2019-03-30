package pl.longhorn.komwojazWorker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class KomwojazMainApplication {

    public static final String PATH_TO_WORKER_CLASS = "C:\\Users\\jaksa\\Desktop\\komwojazWorker\\target\\komwojazMain-0.0.1-SNAPSHOT.jar";
    public static final int TARGET_POINTS_AMMOUNT = 10;
    public static final int POPULATION_AMMOUNT = 10;
    public static final int MAX_GENERATION = 10;
    public static final int CROSSING_PER_GENERATION = 2;
    public static final int MUTATION_PER_GENERATION = 1;
    public static final Random random = new Random();
    public static final List<TargetPoint> targetPoints = generateTargetPoints();

    public static final Servers servers = new Servers();

    public static List<Solution> examinatedSolutions;

    public static void main(String[] args) {
        SpringApplication.run(KomwojazMainApplication.class, args);


        examinatedSolutions = generateInitialSolutions(targetPoints);

        servers.addServer("http://localhost:8080");
        servers.addServer("http://localhost:8044");

        examinateQuality(examinatedSolutions, servers, targetPoints);

        for (int generation = 0; generation < MAX_GENERATION; generation++) {
            runCrossing();
            runMutation();
            runSelection();
        }
        printResults();
    }

    private static void printResults() {
        Solution bestSolution = getSolutionOrWait(0);
        for(int i = 1; i < examinatedSolutions.size(); i++){
            Solution nextSolution = getSolutionOrWait(i);
            if(nextSolution.getQuality() < bestSolution.getQuality()){
                bestSolution = nextSolution;
            }
        }
        System.out.println("Najlepsza kolejnosc: " + bestSolution.getOrder());
        System.out.println("Odleglosc to " + bestSolution.getQuality());
    }

    private static void runSelection() {
        Solution weakestSolution = getSolutionOrWait(0);
        for(int i = 1; i < examinatedSolutions.size(); i++){
            Solution nextSolution = getSolutionOrWait(i);
            if(nextSolution.getQuality() > weakestSolution.getQuality()){
                weakestSolution = nextSolution;
            }
        }
        examinatedSolutions.remove(weakestSolution);
    }

    private static Solution getSolutionOrWait(int poz) {
        Solution solution = examinatedSolutions.get(poz);
        while (solution.getQuality() == -1){
            solution = examinatedSolutions.get(poz);
            try {
                System.out.println("czekam na " + solution.getOrder());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        return solution;
    }

    private static void runMutation() {
        for(int i = 0; i < MUTATION_PER_GENERATION; i++){
            Solution randomSolution = examinatedSolutions.get(random.nextInt(examinatedSolutions.size()));
            examinatedSolutions.add(randomSolution.mutate());
            examinateQuality(examinatedSolutions, servers, targetPoints);
        }
    }

    private static void runCrossing() {
        for (int i = 0; i < CROSSING_PER_GENERATION; i++){
            Solution randomSolution = examinatedSolutions.get(random.nextInt(examinatedSolutions.size()));
            Solution randomSolution2 = examinatedSolutions.get(random.nextInt(examinatedSolutions.size()));
            examinatedSolutions.add(randomSolution.crossWith(randomSolution2));
            examinateQuality(examinatedSolutions, servers, targetPoints);
        }
    }

    private static void examinateQuality(List<Solution> examinatedSolutions, Servers servers, List<TargetPoint> targetPoints) {
        for (Solution solution :
                examinatedSolutions) {
            if (solution.getQuality() == -1) {
                servers.examinate(solution, targetPoints);
            }
        }
    }

    private static List<TargetPoint> generateTargetPoints() {
        List<TargetPoint> points = new CopyOnWriteArrayList<>();
        for (int i = 0; i < TARGET_POINTS_AMMOUNT; i++) {
            points.add(TargetPoint.generate());
        }
        return points;
    }

    private static List<Solution> generateInitialSolutions(List<TargetPoint> targetPoints) {
        List<Solution> randomSolution = new CopyOnWriteArrayList<>();
        for (int i = 0; i < POPULATION_AMMOUNT; i++) {
            randomSolution.add(Solution.random(targetPoints));
        }
        return randomSolution;
    }

    public static void addResults(InputData inputData) {
        System.out.println(inputData.getSolution().getOrder()  + " w " + inputData.getSolution().getQuality());
        for (Solution solution :
                examinatedSolutions) {
            if (solution.getOrder().equals(inputData.getSolution().getOrder())) {
                solution.setQuality(inputData.getSolution().getQuality());
            }
        }
    }
}

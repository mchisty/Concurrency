package home.chisty.pipeline;

public class PipelineDemo {

    public static void main(String[] args) throws InterruptedException {
        DataPipeline pipeline = new DataPipeline();
        pipeline.startPipeline();

        // Let it run for a while
        Thread.sleep(1000);

        pipeline.shutdown();

        // Print results
        System.out.println("Processed Results:");
        pipeline.getResults().stream().sorted().forEach(System.out::println);
    }
}

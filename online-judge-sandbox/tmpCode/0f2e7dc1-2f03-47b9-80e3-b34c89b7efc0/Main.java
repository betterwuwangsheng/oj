public class Main {
    private static final long ONE_HOUR = 60 * 60 * 1000L;

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(ONE_HOUR);
        System.out.println("sleep over");
    }
}

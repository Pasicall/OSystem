package shiyan1;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Consumer_Main {

    private static final int MAX_BUFFER_SIZE = 5 ; //定义缓冲区最大容量
    //使用列表定义消费者以及生产者的共享资源
    private static final List<Integer> shareResources = new ArrayList<>();
    //定义缓冲区为满和空时的信号量
    private static Semaphore emptySemaphore = new Semaphore(MAX_BUFFER_SIZE);
    private static Semaphore fullSemaphore = new Semaphore(0);

    //创建生产者线程类
    static class Producer implements Runnable{
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    emptySemaphore.acquire(); //尝试获取空缓冲区的许可
                    synchronized (shareResources) {
                        int item = random.nextInt(100) + 1;  //随机生成一个数据项
                        shareResources.add(item);
                        System.out.println(Thread.currentThread().getName() + " Produce " + item);
                    }
                    fullSemaphore.release(); //释放一个满缓冲
                    Thread.sleep(random.nextInt(400) + 100); //模拟生产的耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //消费者线程
    static class Comsumer implements Runnable {
        public void run() {
            Random random = new Random();
            while (true) {
                try {
                    fullSemaphore.acquire();
                    synchronized (shareResources) {
                        int item = shareResources.remove(0);
                        System.out.println(Thread.currentThread().getName() + " Consume " + item);
                    }
                    emptySemaphore.release();
                    Thread.sleep(random.nextInt(400) + 100); // 模拟消费耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        //创建生产者进程
        for(int i = 0 ; i < 2 ; i ++) {
            Thread Producer = new Thread(new Producer(), "Poducer" + (i+1));
            Producer.start();
        }

        //创建消费者进程
        for(int i = 0 ; i < 3 ; i ++){
            Thread consumerThread = new Thread(new Comsumer(),"Consumer" + (i+1));
            consumerThread.start();
        }
    }
}

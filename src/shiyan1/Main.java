package shiyan1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static volatile boolean running = true;
    public static void main(String[] args){
        Thread[] threads = new Thread[5];

        //创建并启动线程
        for(int i = 0 ; i < 5 ; i ++) {
            String threadName = "Thread-" + (i + 1);
            Runnable worker = new Worker(i + 1, threadName);
            threads[i] = new Thread(worker);
            threads[i].start();
        }
        //主线程每5秒输出一次5个线程的状态
        while(running){
            try{
                Thread.sleep(5000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            for(Thread thread : threads){
                System.out.println(thread.getName()+"状态"+thread.getState());
            }
        }
        //等待所有线程执行完毕
        for(Thread thread : threads){
            try{
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("系统执行完毕");
    }
    static class Worker extends Thread implements Runnable{
        private final int id;
        private final String name;
        public Worker(int id , String name){
            super(name);
            this.id = id;
            this.name = name;
        }

        @Override
        public void run(){
            int count = 0 ;
            while(running && count < 1000){
                synchronized (this){
                    count++;
                    if(count % id == 0 ){
                        System.out.println(getName()+"输出"+count);
                    }
                }
                try{
                   Thread.sleep(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            System.out.println("执行完毕");
        }
    }
}


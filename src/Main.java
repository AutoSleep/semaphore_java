import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * 使用semaphore信号量实现
 */
public class Main {
    LinkedList<Object> list = new LinkedList<Object>();
    //创建三个信号量
    final Semaphore notFull = new Semaphore(10);
    final Semaphore notEmpty = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        Main main = new Main();
        new Thread(main.new Producer()).start();
        new Thread(main.new Consumer()).start();
        new Thread(main.new Producer()).start();
        new Thread(main.new Consumer()).start();
        new Thread(main.new Producer()).start();
        new Thread(main.new Consumer()).start();
        new Thread(main.new Producer()).start();
        new Thread(main.new Consumer()).start();
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                    notFull.acquire();
                    mutex.acquire();
                    list.add(new Object());
                    System.out.println(Thread.currentThread().getName()
                            + "生产者生产，目前总共有" + list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notEmpty.release();
                }
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    notEmpty.acquire();
                    mutex.acquire();
                    list.removeFirst();
                    System.out.println(Thread.currentThread().getName()
                            + "消费者消费，目前总共有" + list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notFull.release();
                }
            }
        }
    }
}


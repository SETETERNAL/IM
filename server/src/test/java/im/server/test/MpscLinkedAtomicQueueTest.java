package im.server.test;

import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscLinkedAtomicQueue;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.*;

public class MpscLinkedAtomicQueueTest {

    Queue<String> mpscQueue = new MpscLinkedAtomicQueue<>();
    Queue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
    ExecutorService producer;
    ExecutorService consumer;
    Object signal = new Object();

    // @Test
    public void performanceTest() throws InterruptedException {
        for(int i = 1000; i <= 100000; i = i * 10) {
            for(int j = 50; j <= 200; j = j + 10) {
                producerAndConsumer(mpscQueue, j, i);
                producerAndConsumer(linkedBlockingQueue,j, i);
            }
        }
    }

    // @Test
    public void mpscArrayQueueTest(){
        MpscArrayQueue<String> queue = new MpscArrayQueue<>(1);
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        String s;
        while ((s = queue.poll()) != null){
            System.out.println(s);
        }
    }

    @Test
    public void cpuCacheTest(){
        int[] arr = new int[64 * 1024 * 1024];
        long start = System.nanoTime();
        for (int i = 0; i < arr.length; i++) {
            arr[i] *= 3;
        }
        System.out.println(System.nanoTime() - start);

        long start2 = System.nanoTime();
        for (int i = 0; i < arr.length; i += 15) {
            arr[i] *= 3;
        }
        System.out.println(System.nanoTime() - start2);
    }

    private void producerAndConsumer(final Queue<String> queue, final int threadCount,final int count) throws InterruptedException {
        threadPoolRest(threadCount);
        Date startDate = new Date();
        for(int i = 0; i < threadCount; i++) {
            producer.execute(() -> {
                for(int j = 0; j < count; j++){
                    queue.add(j + "");
                }
            });
        }
        producer.shutdown();
        consumer.execute(() -> {
            while(queue.poll() != null || !producer.isTerminated()){
            }
            synchronized(signal) {
                signal.notifyAll();
            }
        });
        consumer.shutdown();
        synchronized(signal) {
            signal.wait();
        }
        Date endDate = new Date();
        System.out.println("线程数" + threadCount +"-轮转" + count + "次" + queue.getClass() + "耗时：" + (endDate.getTime() - startDate.getTime()));
    }

    private void threadPoolRest(final int threadCount){
        if(producer == null) {
            producer = Executors.newFixedThreadPool(threadCount);
        }else{
            while(!producer.isTerminated()){}
            producer = Executors.newFixedThreadPool(threadCount);
        }
        if(consumer == null) {
            consumer = Executors.newSingleThreadExecutor();
        }else{
            while(!consumer.isTerminated()){}
            consumer = Executors.newSingleThreadExecutor();
        }
    }

}

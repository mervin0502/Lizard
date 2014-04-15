package me.mervin.project.usr;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest
{

    private static int queueDeep = 4;

    public void createThreadPool()
    {
        /*  
         * 创建线程池，最小线程数为2，最大线程数为4，线程池维护线程的空闲时间为3秒，  
         * 使用队列深度为4的有界队列，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，  
         * 然后重试执行程序（如果再次失败，则重复此过程），里面已经根据队列深度对任务加载进行了控制。  
         */ 
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        // 向线程池中添加 10 个任务
        for (int i = 0; i < 10; i++)
        {
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            while (getQueueSize(tpe.getQueue()) >= queueDeep)
            {
                System.out.println("队列已满，等3秒再添加任务");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            TaskThreadPool ttp = new TaskThreadPool(i);
            System.out.println("put i:" + i);
            tpe.execute(ttp);
        }

        tpe.shutdown();
    }

    private synchronized int getQueueSize(Queue queue)
    {
        return queue.size();
    }

    public static void main(String[] args)
    {
        ThreadPoolExecutorTest test = new ThreadPoolExecutorTest();
        test.createThreadPool();
    }

    class TaskThreadPool implements Runnable
    {
        private int index;

        public TaskThreadPool(int index)
        {
            this.index = index;
        }

        public void run()
        {
            System.out.println(Thread.currentThread() + " index:" + index);
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}

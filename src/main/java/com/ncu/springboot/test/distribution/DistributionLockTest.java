package com.ncu.springboot.test.distribution;

import com.ncu.springboot.aop.ApiInvokeTimeShow;
import com.ncu.springboot.mvc.controller.UserController;
import com.ncu.springboot.pojo.User;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DistributionLockTest {
    private  UserController userController;
    public DistributionLockTest(UserController userController) {
        this.userController = userController;
    }

    @ApiInvokeTimeShow(methodName = "redis分布式锁测试1")
    public void test1() {
        int count = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++)
            executorService.execute(this.new Task1("name"+i));

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Task1 implements Runnable {
        private String name;
        Task1 (String name)  {
            this.name=name;
        }
        @Override
        public void run() {
            try {
                userController.save(new User(name,"982363676@qq.com","123")) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @ApiInvokeTimeShow(methodName = "redis分布式锁测试2,模拟真正并发")
    public  void test2() {
        int count = 10000;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++)
            executorService.execute(this.new Task2(cyclicBarrier, "name"+i));

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Task2 implements Runnable {
        private CyclicBarrier cyclicBarrier;
        private String name;
        Task2(CyclicBarrier cyclicBarrier, String name) {
            this.cyclicBarrier = cyclicBarrier;
            this.name=name;
        }
        @Override
        public void run() {
            try {
                // 等待所有任务准备就绪
                cyclicBarrier.await();
                userController.save(new User(name,"982363676@qq.com","123")) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.cs278.schoolcraig.test;

import android.os.Looper;

import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.api.TaskCallback;

import junit.framework.TestCase;
import java.util.concurrent.Callable;

/**
 * Created by violettavylegzhanina on 12/2/14.
 */
public class CallableTaskTest extends TestCase{

    private boolean notified = false;
    private boolean backgroundInNonUIThread = false;

    public void testInvokeNoError() throws Exception {

        final Object lock = new Object();

        CallableTask.invoke(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                backgroundInNonUIThread = Looper.myLooper() != Looper.getMainLooper();
                return null;
            }
        }, new TaskCallback<Object>() {
            @Override
            public void success(Object result) {
                synchronized (lock) {
                    // check that we are on UI thread
                    notified = Looper.myLooper() == Looper.getMainLooper();
                    lock.notify();
                }
            }

            @Override
            public void error(Exception e) {
                synchronized (lock) {
                    notified = false;
                    lock.notify();
                }
            }
        });

        synchronized (lock) {

            lock.wait(1000);

            if(!backgroundInNonUIThread){
                fail("The background work ran in UI thread!!!");
            }
            else if(!notified){
                fail("Failed to be notified of the background task's result (or an error occurred)");
            }
        }
    }


    public void testInvokeWithError() throws Exception {

        final Object lock = new Object();

        CallableTask.invoke(new Callable<Object>(){
            @Override
            public Object call() throws Exception {
                backgroundInNonUIThread = Looper.myLooper() != Looper.getMainLooper();
                throw new RuntimeException("Should be caught and passed to error handler");
            }
        }, new TaskCallback<Object>() {
            @Override
            public void success(Object result) {
                synchronized (lock){
                    // Make test fail
                    notified = false;
                    lock.notify();
                }
            }

            @Override
            public void error(Exception e) {
                synchronized (lock){
                    // check that we are on the UI thread
                    notified = Looper.myLooper() == Looper.getMainLooper();
                    lock.notify();
                }
            }
        });

        synchronized (lock) {
            lock.wait(1000);

            if(!backgroundInNonUIThread){
                fail("The background work ran in UI thread!!!");
            }
            else if(!notified){
                fail("Failed to notify the exception");
            }
        }
    }
}

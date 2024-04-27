package com.trinhxuantam.threadsafesinglylinkedlist.utils.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public abstract class LockableResource {
    private final ReadWriteLock lock;
    
    private final Lock readLock;
    
    private final Lock writeLock;

    protected LockableResource() {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
    }

    protected <R> R withReadLock(Supplier<R> action) {
        readLock.lock();
        try {
            return action.get();
        } finally {
            readLock.unlock();
        }
    }

    protected <R> R withWriteLock(Supplier<R> action) {
        writeLock.lock();
        try {
            return action.get();
        } finally {
            writeLock.unlock();
        }
    }

    protected void withWriteLock(Runnable action) {
        writeLock.lock();
        try {
            action.run();
        } finally {
            writeLock.unlock();
        }
    }

    protected void withReadLock(Runnable action) {
        readLock.lock();
        try {
            action.run();
        } finally {
            readLock.unlock();
        }
    }
}

package top.guoziyang.mydb.backend.dm.pageCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import top.guoziyang.mydb.backend.dm.page.MockPage;
import top.guoziyang.mydb.backend.dm.page.Page;

public class MockPageCache implements PageCache {

    private final Map<Integer, MockPage> cache = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    private final AtomicInteger noPages = new AtomicInteger(0);
    
    @Override
    public int newPage(byte[] initData) {
        lock.lock();
        try {
            int pgno = noPages.incrementAndGet();
            MockPage pg = MockPage.newMockPage(pgno, initData);
            cache.put(pgno, pg);
            return pgno;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Page getPage(int pgno) {
        lock.lock();
        try {
            return cache.get(pgno);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {}

    @Override
    public void release(Page page) {}

    @Override
    public void truncateByBgno(int maxPgno) {}

    @Override
    public int getPageNumber() {
        return noPages.intValue();
    }

    @Override
    public void flushPage(Page pg) {}
    
}

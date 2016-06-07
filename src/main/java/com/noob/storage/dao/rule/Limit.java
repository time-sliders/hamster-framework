package com.noob.storage.dao.rule;

/**
 * 数据库查询limit条件
 *
 * @author luyun
 * @since 2016.06.07
 */
public class Limit {

    private static final int DEFAULT_LIMIT = 10;
    
    private static final int DEFAULT_START = 0;

    private int start = DEFAULT_START;

    private int end = DEFAULT_LIMIT;

    public Limit(int start, int end) {

        start = start < 0 ? DEFAULT_START : start;
        end = end <= 0 ? DEFAULT_LIMIT : end;

        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

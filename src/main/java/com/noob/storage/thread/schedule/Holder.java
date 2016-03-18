package com.noob.storage.thread.schedule;

public abstract class Holder {

    /**
     * 当前持有者所占有的资源
     */
    protected int holdNum;

    /**
     * 当前持有者对资源的渴望程度
     */
    protected int needForReSource;

    protected Scheduler scheduler;

    public Holder() {
        this(0);
    }

    /**
     * @param needForReSource 资源的渴望度
     */
    public Holder(int needForReSource) {
        this.needForReSource = needForReSource;
    }

    public void reInit(int holdNum) {
        reSize(holdNum);
        this.holdNum = holdNum;
    }

    /**
     * 重新设置资源值
     * 如：当资源位线程时,该方法需要调整线程池的大小
     */
    protected abstract void reSize(int num);

    /**
     * 子类重写该方法
     * 实现根据线程的执行情况，动态调成资源渴望值(needForReSource)
     */
    protected abstract void calculateNeed();

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public int getNeedForReSource() {
        return needForReSource;
    }

    public void setNeedForReSource(int needForReSource) {
        this.needForReSource = needForReSource;
    }

    public void release() {
        scheduler.release(this);
    }

    public int getHoldNum() {
        return holdNum;
    }
}

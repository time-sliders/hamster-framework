package com.noob.storage.thread.schedule;

public abstract class Holder {

    /**
     * 当前持有者所占有的资源
     */
    protected int holdNum;

    /**
     * 当前持有者对资源的渴望程度
     */
    protected int proportion;

    protected Scheduler scheduler;

    public Holder() {
        this(0);
    }

    /**
     * @param proportion 资源的渴望度
     */
    public Holder(int proportion) {

        if (proportion < 0) {
            throw new IllegalArgumentException("proportion must big than 0!");
        }

        this.proportion = proportion;
    }

    public void reInit(int holdNum) {

        if (holdNum < 0) {
            throw new IllegalArgumentException("holdNum must big than 0!");
        }

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
     * 实现根据线程的执行情况，动态调成资源渴望值(proportion)
     */
    protected abstract void calculateNeed();

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    public void release() {
        scheduler.release(this);
    }

    public int getHoldNum() {
        return holdNum;
    }
}

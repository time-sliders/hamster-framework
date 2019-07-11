package com.noob.storage.other.benefit;

import java.util.List;

/**
 * @author 卢云(luyun)
 * @version hello java
 * @since 2019.07.11
 */
public abstract class AbstractComplexInvest {

    /**
     * 将一笔复杂投资拆解为多笔原子投资AtomicInvest
     */
    public abstract List<AtomicInvest> decompose();

}

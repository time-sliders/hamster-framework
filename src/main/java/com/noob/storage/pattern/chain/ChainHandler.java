package com.noob.storage.pattern.chain;

/**
 * 职责链的处理器单元
 *
 * @author luyun
 * @since app5.9
 */
public interface ChainHandler {

    void execute(ChainContext context);
}

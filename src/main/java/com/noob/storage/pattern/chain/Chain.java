package com.noob.storage.pattern.chain;

import java.util.LinkedList;
import java.util.List;

/**
 * 责任链
 *
 * @author luyun
 * @since app5.9
 */
public class Chain implements ChainHandler{

    private List<ChainHandler> handlerList = new LinkedList<ChainHandler>();

    public void execute(ChainContext context){
        for(ChainHandler handler:handlerList){
            try {
                handler.execute(context);
                if(!context.isInProcessing()){
                    break;
                }
            } catch (Throwable e) {
                context.saveException(e);
                break;
            }
        }
    }

    public void addHandler(ChainHandler handler) {
        handlerList.add(handler);
    }

    public void removeHandler(ChainHandler handler){
        handlerList.remove(handler);
    }

}

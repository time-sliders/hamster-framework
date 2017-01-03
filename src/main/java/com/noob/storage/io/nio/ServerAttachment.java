package com.noob.storage.io.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Set;

/**
 * 服务端SelectionKey的Attachment
 *
 * @author luyun
 */
public class ServerAttachment {

    /*
     * the selection key associate with this
     */
    private SelectionKey selectionKey;

    public ServerAttachment(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void doSelect(){
        Set<SelectionKey> selectionKeySet = selectionKey.selector().selectedKeys();


    }
}

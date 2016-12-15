package com.noob.storage.component.cache.loader;

/**
 * Map 类型数据的缓存配置
 *
 * @author luyun
 */
public class MapDataLoader extends DataLoader {

    /*
     * 这个Map在缓存中的可以
     */
    private String mapKey;

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }
}

package cn.uestc.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import cn.uestc.common.BootLoader;
import cn.uestc.dataserver;

//记录每个dataserver现有块的数量
public class BlockManager {
    private static Map<dataserver,Long> everyBlockCounts= new ConcurrentHashMap<>();

    public static void addBlockCount(dataserver dataserver, long blockCount){
        everyBlockCounts.put(dataserver,blockCount);
    }
    //更新blockCount的值
    public static void updateBlockCount(String ip, long blockCount) {
        Set<Map.Entry<dataserver, Long>> entries = everyBlockCounts.entrySet();
        for (Map.Entry<dataserver, Long> entry : entries) {
            if (entry.getKey().getIp().equals(ip)) {
                entry.setValue(blockCount);
                break;
            }
        }
    }
    //获取
    public static Map<dataserver,Long> getEveryBlockCounts(){
        return everyBlockCounts;
    }
    public static void sortByValue(Map<dataserver,Long> map) {
        if(map.size()<=1) return;
        Map<dataserver,Long> sortedMap = new HashMap<>();
        for (Map.Entry<dataserver, Long> entry : map.entrySet()) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        sortedMap.entrySet().stream()
                .sorted(Comparator.comparingLong(e -> e.getValue()))
                .forEach(e -> everyBlockCounts.put(e.getKey(), e.getValue()));
    }
    //列出当前每个dataserver的块数量
    public static void printAllKeysAndValues(Map<dataserver,Long> map) {
        Iterator<Map.Entry<dataserver,Long>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<dataserver, Long> entry = iterator.next();
            System.out.println("dataerverId:" + entry.getKey().toString() + ", FreeBlockCounts:" + entry.getValue());
        }
    }

    public static void removebyIP(String ip) {
        Map<dataserver, Long> everyBlockCounts1 = getEveryBlockCounts();
        Set<dataserver> set = everyBlockCounts1.keySet();
        for(dataserver dataserver: set) {
            if (dataserver.getIp().equals(ip)) {
                everyBlockCounts1.remove(dataserver);
            }
        }
    }
}

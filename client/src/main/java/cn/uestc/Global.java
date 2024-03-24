package cn.uestc;


import cn.uestc.entity.BlockInfo;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static volatile int  BlockCounts = 0;
    public static volatile List<BlockInfo> blockInfos= new ArrayList<>();
    public static volatile String filePathTarget=null;
    public static volatile List<Integer> transferCounts =new ArrayList<>();
}

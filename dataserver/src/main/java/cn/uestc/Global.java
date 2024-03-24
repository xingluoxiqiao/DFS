
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc;


import cn.uestc.common.BootLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Description: 
 * </p>

 */
public class Global {
    
    public static volatile long freeBlockCount = 0;
    public static int dataserverID =0;
    public static String partitionPath = null;
    public static volatile List<Integer> transferCounts =new ArrayList<>();
    public static volatile List<dataserver> dataservers =new ArrayList<>();
}

    
package cn.uestc.Mapper;

import cn.uestc.entity.BlockInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
public interface UploadMapper {

    /**
     * 将数据插入数据库
     * @param blockInfo
     */
    @Insert("INSERT INTO BlockInfo (uuid, dataserverIp1, dataserverPort1, dataserverIp2, " +
            "dataserverPort2, dataserverIp3, dataserverPort3, fileName, createTime, updateTime, totalBlockCounts) " +
            "VALUES (#{uuid}, #{dataserverIp1}, #{dataserverPort1}, #{dataserverIp2}," +
            " #{dataserverPort2}, #{dataserverIp3}, #{dataserverPort3}, #{fileName}, " +
            "#{createTime}, #{updateTime}, #{totalBlockCounts})")
    void insert(BlockInfo blockInfo);

    /**
     * 通过uuid获取文件信息，能获取返回true
     * @param uuid
     * @return
     */
    @Select("SELECT * FROM blockinfo WHERE uuid = #{uuid}")
    BlockInfo select(String uuid);


    /**
     * 通过文件名获取文件信息
     * @param filename
     * @return
     */
    @Select("SELECT * FROM blockinfo WHERE fileName = #{filename}")
    List<BlockInfo> selectByName(String filename);

    /**
     * 将文件名称存入数据库中
     * @param filename
     * @param uuid
     */
    @Update("UPDATE blockinfo SET fileName = #{filename} WHERE uuid = #{uuid}")
    void updateFileName(@Param("filename") String filename,@Param("uuid") String uuid);

    @Update("UPDATE blockinfo SET totalBlockCounts = #{totalBlockCounts} WHERE uuid = #{uuid}")
    void updateTotalBlockCounts(@Param("totalBlockCounts") int  totalBlockCounts,@Param("uuid") String uuid);

    @Select("SELECT fileName FROM blockinfo")
    List<String> listFiles();
}


package cn.uestc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uuid;
    private String dataserverIp1;
    private int dataserverPort1;
    private String dataserverIp2;
    private int dataserverPort2;
    private String dataserverIp3;
    private int dataserverPort3;
    private String fileName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int totalBlockCounts;

    public String toString(){
        return "BlockInfo{" +
                "uuid='" + uuid + '\'' +
                ", dataserverIp1='" + dataserverIp1 + '\'' +
                ", dataserverPort1=" + dataserverPort1 +
                ", dataserverIp2='" + dataserverIp2 + '\'' +
                ", dataserverPort2=" + dataserverPort2 +
                ", dataserverIp3='" + dataserverIp3 + '\'' +
                ", dataserverPort3=" + dataserverPort3 +
                ", fileName='" + fileName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", totalBlockCounts=" + totalBlockCounts +
                '}';
    }
}

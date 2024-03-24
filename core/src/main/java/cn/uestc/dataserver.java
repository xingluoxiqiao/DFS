package cn.uestc;

import java.io.Serializable;
import java.util.Objects;

public class dataserver implements Serializable {
    private  static final long serialVersionUID = 1L;
    private int port;
    private String ip;
    private String id;


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public dataserver( String ip,int port,String id) {
        this.port = port;
        this.ip = ip;
        this.id = id;
    }

    // 重写 equals() 方法，使得两个 DataServer 对象只要 id 一致就是同一个对象
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        dataserver other = (dataserver) obj;
        return id == other.id;
    }

    // 重写 hashCode() 方法，使得两个 DataServer 对象只要 id 一致就是同一个对象
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 重写 toString() 方法，方便打印 DataServer 对象的信息
    @Override
    public String toString() {
        return "DataServer{" +
                "port=" + port +
                ", ip='" + ip + '\'' +
                '}';
    }
}

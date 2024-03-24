package cn.uestc;
import cn.uestc.Mapper.UploadMapper;
import cn.uestc.common.NettyClientBootstrap;
import cn.uestc.common.message.RequestMsg.readDataRequestMsg;
import cn.uestc.common.message.RequestMsg.writeRequestMsg;
import cn.uestc.entity.BlockInfo;
import cn.uestc.exception.CommonException;
import cn.uestc.utils.myFileUtils;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cn.uestc.common.BootLoader.getProperties;
import static cn.uestc.common.StatusCodeEnum.FILE_READ_ERROR;
import static cn.uestc.common.StatusCodeEnum.FILE_WRITE_ERROR;
import static cn.uestc.common.Unit.KB;
import static cn.uestc.common.initConfiguration.*;

@Service
@Component
@Slf4j
public class MainHelper {

    @Autowired
    private UploadMapper uploadMapper;

    public Boolean upload(String path) throws InterruptedException, IOException {
        // 生成一个uuid
        String uuid = UUID.randomUUID().toString();
        File file =new File(path);
        String filename = file.getName();
        //   获取文件大小
        Path paths = Paths.get(path);
        long size = Files.size(paths);
        int blocksize= BLOCK_SIZE*KB;
        int BlockCounts=(int)size/blocksize;
        //分割文件，存储进相对路径下的uuid + "blocks"，块文件命名为"block_" + blockNumber + ".bin"
        myFileUtils.splitFile(path, blocksize, uuid);
        // 创建一个NettyClientBootstrap实例，用于创建客户端
        NettyClientBootstrap dataClient = new NettyClientBootstrap(getProperties("NAMESERVER_IP"),
                getProperties("NAMESERVER_PORT"), new NettyClientHandler(), "client 的 name client 正在启动");
        // 调用dataClient的getSocketChannel方法，创建一个客户端连接，并向远端发送创建块消息
        SocketChannel socketChannel = dataClient.getSocketChannel();
        socketChannel.writeAndFlush(new writeRequestMsg(BlockCounts,uuid));
        log.info("已发送请求");
        dataClient.getSocketChannel().closeFuture().sync();
        uploadMapper.insert(Global.blockInfos.get(0));
        uploadMapper.updateFileName(filename, uuid);
        //在第一块增加总块数信息
        uploadMapper.updateTotalBlockCounts((int)BlockCounts+1,uuid);
        //删除临时文件夹
        myFileUtils.deleteFile(uuid);
        Global.blockInfos=new ArrayList<>();
        return  true;
    }

    public Boolean download(String filename,String filepathtarget) throws InterruptedException, IOException {

        // 根据文件名称获取dataserverip
        List<BlockInfo> blockInfos = uploadMapper.selectByName(filename);
        if (blockInfos.isEmpty()){
            throw  new CommonException(FILE_READ_ERROR);
        }
        String[] ips=new String[3];
        int[] ports=new int[3];
        BlockInfo blockInfo = blockInfos.get(0);
        ips[0]=blockInfo.getDataserverIp1();
        ports[0]=blockInfo.getDataserverPort1();
        ips[1]=blockInfo.getDataserverIp2();
        ports[1]=blockInfo.getDataserverPort2();
        ips[2]=blockInfo.getDataserverIp3();
        ports[2]=blockInfo.getDataserverPort3();
        int totalBlockCounts = blockInfo.getTotalBlockCounts();
        String uuid=blockInfo.getUuid();
        //创建文件夹存放下载的文件
        Global.filePathTarget=filepathtarget;
        for (int i = 0; i < ips.length; i++) {
            if (ips[i] != null && ports[i] != 0) {
                try {
                    NettyClientBootstrap dataClient = new NettyClientBootstrap(ips[i],
                            String.valueOf(ports[i]), new NettyClientHandler(), "client 的 data client 正在启动");
                    SocketChannel socketChannel1 = dataClient.getSocketChannel();
                    socketChannel1.writeAndFlush(new readDataRequestMsg(uuid));
                    dataClient.getSocketChannel().closeFuture().sync();
                    // 如果连接和获取资源成功，跳出循环
                    break;
                } catch (Exception e) {
                    // 发生异常，继续尝试下一个服务器
                    System.out.println("当前dataserver不存在该文件，正在重新获取" + e.getMessage());
                }
            }
        }
        //到这里应该是所有的dataserver均完成传输，filePathTarget中已收集到所有块
        String blockSourcePath= Global.filePathTarget+File.separator+"blocks";
        if(myFileUtils.countFilesInFolder(blockSourcePath)==totalBlockCounts){
            myFileUtils.fileMerge(blockSourcePath,filepathtarget+File.separator+filename,uuid);
        }else{
            //删除临时文件夹中的内容
            myFileUtils.deleteAllFilesInFolder(blockSourcePath);
            throw new CommonException(FILE_READ_ERROR);
        }
        return true;
    }
    public void listFiles() {
        List<String> fileNames = uploadMapper.listFiles();
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }

}


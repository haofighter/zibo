package com.szxb.zibo.config.zibo;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class FastFTSTrakerClient extends TrackerClient {

    public FastFTSTrakerClient(TrackerGroup tracker_group) {
        super(tracker_group);
    }

    @Override
    public StorageServer getStoreStorage(TrackerServer trackerServer, String groupName) throws IOException {
        boolean bNewConnection;
        if (trackerServer == null) {
            trackerServer = this.getConnection();
            if (trackerServer == null) {
                return null;
            }

            bNewConnection = true;
        } else {
            bNewConnection = false;
        }

        Socket trackerSocket = trackerServer.getSocket();
        OutputStream out = trackerSocket.getOutputStream();

        StorageServer var28;
        try {
            byte cmd;
            byte out_len;
            if (groupName != null && groupName.length() != 0) {
                cmd = 104;
                out_len = 16;
            } else {
                cmd = 101;
                out_len = 0;
            }

            byte[] header = ProtoCommon.packHeader(cmd, (long) out_len, (byte) 0);
            out.write(header);
            if (groupName != null && groupName.length() > 0) {
                byte[] bs = groupName.getBytes(ClientGlobal.g_charset);
                byte[] bGroupName = new byte[16];
                int group_len;
                if (bs.length <= 16) {
                    group_len = bs.length;
                } else {
                    group_len = 16;
                }

                Arrays.fill(bGroupName, (byte) 0);
                System.arraycopy(bs, 0, bGroupName, 0, group_len);
                out.write(bGroupName);
            }

            ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(), (byte) 100, 40L);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                var28 = null;
                return var28;
            }

            String ip_addr = (new String(pkgInfo.body, 16, 15)).trim();
            int port = (int) ProtoCommon.buff2long(pkgInfo.body, 31);
            byte store_path = pkgInfo.body[39];
            var28 = new StorageServer("139.9.113.219", 22001, store_path);
        } catch (IOException var25) {
            if (!bNewConnection) {
                try {
                    trackerServer.close();
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            }

            throw var25;
        } finally {
            if (bNewConnection) {
                try {
                    trackerServer.close();
                } catch (IOException var23) {
                    var23.printStackTrace();
                }
            }

        }

        return var28;
    }
}

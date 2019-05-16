package com.min.hbase.example;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

@Slf4j
public class HbaseUtil {
    @Getter
    private static Connection connection = null;
    @Getter
    private static Admin admin = null;

    public static void initHbase(String quorum, String port) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        //集群配置↓ (zk集群IP组和zk端口)
        configuration.set("hbase.zookeeper.quorum", quorum);
        configuration.set("hbase.zookeeper.property.clientPort", port);
        log.info("hbase.zookeeper.quorum", quorum);
        log.info("hbase.zookeeper.property.clientPort", port);
        HbaseUtil.connection = ConnectionFactory.createConnection(configuration);
        HbaseUtil.admin = connection.getAdmin();
    }

    /**
     * 创建表
     */
    public boolean createTable(String tableName, List<String> columnFamily) {
        return this.createTable(tableName, columnFamily, null);
    }

    /**
     * 预分区创建表
     *
     * @param tableName    表名
     * @param columnFamily 列族名的集合
     * @param splitKeys    预分期region
     * @return 是否创建成功
     */
    public boolean createTable(String tableName, List<String> columnFamily, byte[][] splitKeys) {
        try {
            //判断表是否存在
            if (admin.tableExists(TableName.valueOf(tableName))) {
                log.info("table Exists!" + tableName);
                return false;
            } else {
                //构建列族
                List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<>(columnFamily.size());
                columnFamily.forEach(cf -> {
                    familyDescriptors.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build());
                });
                //构建表
                TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
                        .setColumnFamilies(familyDescriptors)
                        .build();
                if (splitKeys != null) {
                    admin.createTable(tableDescriptor, splitKeys);
                } else
                    admin.createTable(tableDescriptor);
                log.info("create table Success!");
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("", tableName), e);
            return false;
        } finally {
            close(admin, null, null);
        }
        return true;
    }


    /**
     * 自定义获取分区splitKeys
     */
    public byte[][] getSplitKeys(String[] keys) {
        if (keys == null) {
            //默认为10个分区
            keys = new String[]{"1|", "2|", "3|", "4|",
                    "5|", "6|", "7|", "8|", "9|"};
        }
        byte[][] splitKeys = new byte[keys.length][];
        //升序排序
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);
        for (String key : keys) {
            rows.add(Bytes.toBytes(key));
        }

        Iterator<byte[]> rowKeyIter = rows.iterator();
        int i = 0;
        while (rowKeyIter.hasNext()) {
            byte[] tempRow = rowKeyIter.next();
            rowKeyIter.remove();
            splitKeys[i] = tempRow;
            i++;
        }
        return splitKeys;
    }

    /**
     * 按startKey和endKey，分区数获取分区
     */
    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[] b = String.format("%016x", key).getBytes();
            splits[i] = b;
        }
        return splits;
    }

    /**
     * 查询库中所有表的表名
     */
    public List<String> getAllTableNames() {
        List<String> result = new ArrayList<>();
        try {
            TableName[] tableNames = this.admin.listTableNames();
            for (TableName tableName : tableNames) {
                result.add(tableName.getNameAsString());
            }
        } catch (IOException e) {
            log.error("获取所有表的表名失败", e);
        } finally {
            close(admin, null, null);
        }

        return result;
    }


    //插入数据
    public static void insertData(String tableName, User user) throws IOException {
        TableName tablename = TableName.valueOf(tableName);
        Put put = new Put("rowsKey".getBytes());
        //参数：1.列族名  2.列名  3.值
        put.addColumn("information".getBytes(), "username".getBytes(), user.getUsername().getBytes());
        put.addColumn("information".getBytes(), "age".getBytes(), user.getAge().getBytes());
        put.addColumn("information".getBytes(), "gender".getBytes(), user.getGender().getBytes());
        put.addColumn("contact".getBytes(), "phone".getBytes(), user.getPhone().getBytes());
        put.addColumn("contact".getBytes(), "email".getBytes(), user.getEmail().getBytes());
        //HTable table = new HTable(initHbase().getConfiguration(),tablename);已弃用
        Table table = connection.getTable(tablename);
        table.put(put);
    }

    /**
     * 关闭流
     */
    private void close(Admin admin, ResultScanner rs, Table table) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                log.error("关闭Admin失败", e);
            }
        }

        if (rs != null) {
            rs.close();
        }

        if (table != null) {
            try {
                table.close();
            } catch (IOException e) {
                log.error("关闭Table失败", e);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        HbaseUtil crud = new HbaseUtil();
        HbaseUtil.initHbase("192.168.0.61,192.168.0.62,192.168.0.63,192.168.0.64,192.168.0.65", "2181");
        List<String> allTableNames = crud.getAllTableNames();
        System.out.println(allTableNames);
    }

}

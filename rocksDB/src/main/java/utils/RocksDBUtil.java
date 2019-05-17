package utils;

import org.rocksdb.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RocksDB存储工具类
 * 默认列族：default
 *
 * rocksDB 是一个可嵌入的，持久性的 key-value存储。基于Google的LevelDB，但提高了扩展性可以运行在多核处理器上，可以有效使用快速存储，支持IO绑定、内存和一次写负荷
 *
 * RocksDB特点：
 * 高性能：RocksDB使用一套日志结构的数据库引擎，为了更好的性能，这套引擎是用C++编写的。 Key和value是任意大小的字节流。
 * 为快速存储而优化：RocksDB为快速而又低延迟的存储设备（例如闪存或者高速硬盘）而特殊优化处理。 RocksDB将最大限度的发挥闪存和RAM的高度率读写性能。
 * 可适配性：RocksDB适合于多种不同工作量类型。从像MyRocks这样的数据存储引擎，到应用数据缓存,甚至是一些嵌入式工作量，RocksDB都可以从容面对这些不同的数据工作量需求。
 * 基础和高级的数据库操作，RocksDB提供了一些基础的操作，例如打开和关闭数据库。对于合并和压缩过滤等高级操作，也提供了读写支持。
 */
public class RocksDBUtil {
    private RocksDB rocksDB;
    //原有列族集
    private List<ColumnFamilyDescriptor> columnFamilyDescriptors = new ArrayList<>();
    //配置
    private Options options = new Options();
    private DBOptions dbOptions = new DBOptions();
    //DB存储位置
    private String dbPath = "rocksDB/src/main/java/utils/store";
    //现有列族集
    private List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>();


    private void init() throws IOException, RocksDBException {
        RocksDB.loadLibrary();
        //创建数据库
        options.setCreateIfMissing(true);

        if (!Files.isSymbolicLink(Paths.get(dbPath))) Files.createDirectories(Paths.get(dbPath));
        //查询现有的所有列族添加到列族描述中若没有添加默认列族
        List<byte[]> cfs = RocksDB.listColumnFamilies(options, dbPath);
        if (cfs.size() > 0) {
            for (byte[] cf : cfs) {
                columnFamilyDescriptors.add(new ColumnFamilyDescriptor(cf, new ColumnFamilyOptions()));
            }
        } else {
            columnFamilyDescriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, new ColumnFamilyOptions()));
        }

        dbOptions.setCreateIfMissing(true);
        rocksDB = RocksDB.open(dbOptions, dbPath, columnFamilyDescriptors, columnFamilyHandles);
    }

    /**
     * 获取所有列族
     */
    public List<String> getColumnFamilies() throws RocksDBException {
        return RocksDB.listColumnFamilies(options, dbPath).stream().map(String::new).collect(Collectors.toList());
    }

    /**
     * 判断列族是否存在
     */
    public boolean ColumnFamilyIsEsist(String columnFamily) throws RocksDBException {
        for (ColumnFamilyDescriptor columnFamilyDescriptor : columnFamilyDescriptors) {
            if (new String(columnFamilyDescriptor.columnFamilyName()).equals(columnFamily)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除一个列族
     */
    public boolean dropColumnFamily(String columnFamily) throws RocksDBException {
        for (int i = 0; i < columnFamilyDescriptors.size(); i++) {
            if (new String(columnFamilyDescriptors.get(i).columnFamilyName()).equals(columnFamily)) {
                rocksDB.dropColumnFamily(columnFamilyHandles.get(i));
                return true;
            }
        }
        return false;
    }

    /**
     * 创建一个列族集
     */
    public ColumnFamilyHandle createColumnFamily(String columnFamily) throws RocksDBException {
        for (int i = 0; i < columnFamilyDescriptors.size(); i++) {
            if (new String(columnFamilyDescriptors.get(i).columnFamilyName()).equals(columnFamily)) {
                columnFamilyHandles.add(columnFamilyHandles.get(i));
                return columnFamilyHandles.get(i);
            }
        }
        ColumnFamilyHandle cf = rocksDB.createColumnFamily(new ColumnFamilyDescriptor(columnFamily.getBytes(), new ColumnFamilyOptions()));
        columnFamilyHandles.add(cf);
        return cf;
    }

    /*****************************************************CRUD操作*********************************************************************************/

    public void save(byte[] key, byte[] value) throws RocksDBException {
        rocksDB.put(key, value);
    }

    public void save(ColumnFamilyHandle columnFamilyHandle, byte[] key, byte[] value) throws RocksDBException {
        rocksDB.put(columnFamilyHandle, key, value);
    }

    public byte[] get(byte[] key) throws RocksDBException {
        return rocksDB.get(key);
    }

    public byte[] get(ColumnFamilyHandle columnFamilyHandle, byte[] key) throws RocksDBException {
        return rocksDB.get(columnFamilyHandle, key);
    }

    public Map<byte[], byte[]> get(List<byte[]> list) throws RocksDBException {
        return rocksDB.multiGet(list);
    }

    public Map<byte[], byte[]> get(List<byte[]> list, List<ColumnFamilyHandle> handleList) throws RocksDBException {
        return rocksDB.multiGet(handleList, list);
    }

    public RocksIterator getAll() {
        return rocksDB.newIterator();
    }

    public RocksIterator getAll(ColumnFamilyHandle columnFamilyHandle) {
        return rocksDB.newIterator(columnFamilyHandle);
    }

    public void delete(byte[] key) throws RocksDBException {
        rocksDB.delete(key);
    }

    public void delete(ColumnFamilyHandle columnFamilyHandle, byte[] key) throws RocksDBException {
        rocksDB.delete(columnFamilyHandle, key);
    }

    public void close(){
        for(ColumnFamilyHandle columnFamilyHandle:columnFamilyHandles){
            columnFamilyHandle.close();
        }
        options.close();
        dbOptions.close();
        rocksDB.close();
    }

    public static void main(String[] args) throws IOException, RocksDBException {
        RocksDBUtil rocksDBUtils = new RocksDBUtil();
        rocksDBUtils.init();
/*        RocksIterator iter = rocksDBUtils.getAll();
        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
        }*/
        ColumnFamilyHandle aNew = rocksDBUtils.createColumnFamily("new");
        List<String> columnFamilies = rocksDBUtils.getColumnFamilies();
        System.out.println(columnFamilies);
        rocksDBUtils.close();
    }
}

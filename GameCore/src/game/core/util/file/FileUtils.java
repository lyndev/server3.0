package game.core.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.core.util.StringUtils;

/**
 *
 * <b>文件(目录)操作的助手类.</b>
 * <p>
 * 此类封装了一些常用的文件操作.
 * <p>
 * <b>Sample:</b>
 * <p>
 * v1.5增加了FileResultSet来记录某些预定义操作的结果信息, 例如删除文件时, 能在执行后获取更多的信息:
 *
 * <pre>
 * FileResultSet resultSet = FileUtils.delete(&quot;E:\\fileTest\\video&quot;);
 * System.out.println(&quot;成功删除&quot; + resultSet.getTotalFiles() + &quot;个文件.&quot;);
 * System.out.println(&quot;成功删除&quot; + resultSet.getTotalDirectory() + &quot;个目录.&quot;);
 * // 如果有文件未被成功删除
 * if (!resultSet.isSuccessful()) {
 * 	// 获取失败的文件队列
 * 	Iterator it = resultSet.getFailFiles().iterator();
 * 	System.out.println(&quot;以下文件在执行中删除失败: &quot;);
 * 	while (it.hasNext()) {
 * 		System.out.println(it.next());
 * 	}
 * }
 * </pre>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.5.0
 */
public class FileUtils
{

    /**
     * 1M的字节数
     */
    static final int MB = 1024 * 1024;

    /**
     * 1G的字节数
     */
    static final int GB = 1024 * 1024 * 1024;

    /**
     * 1T的字节数
     */
    static final long TB = 1024 * 1024 * 1024 * 1024;

    private FileUtils()
    {
        throw new UnsupportedOperationException("该类不允许被实例化!");
    }

    /**
     * 获取文件前缀名.
     *
     * @param file 目标文件
     * @return 去掉后缀名的文件名称
     * @throws FileNotFoundException
     */
    public static String getPrefixName(File file) throws FileNotFoundException
    {
        checkExistsAndIsFile(file);
        String name = StringUtils.lastIndexOfBefore(file.getName(), '.');
        return null != name ? name : file.getName();
    }

    /**
     * 获取文件前缀名.
     *
     * @param filename 目标文件的全路径
     * @return 去掉后缀名的文件名称
     * @throws FileNotFoundException
     */
    public static String getPrefixName(String filename)
            throws FileNotFoundException
    {
        return getPrefixName(new File(filename));
    }

    /**
     * 获取文件后缀名.
     *
     * @param file 目标文件
     * @return 文件的后缀名, 如果文件无后缀名则返回空字符串: ""
     * @throws FileNotFoundException
     */
    public static String getPostfixName(File file) throws FileNotFoundException
    {
        checkExistsAndIsFile(file);
        String postfixName = StringUtils.lastIndexOfAfter(file.getName(), '.');
        if (null == postfixName)
            postfixName = "";

        return postfixName;
    }

    /**
     * 获取文件后缀名.
     *
     * @param filename 目标文件的全路径
     * @return 文件的后缀名, 如果文件无后缀名则返回空字符串: ""
     * @throws FileNotFoundException
     */
    public static String getPostfixName(String filename)
            throws FileNotFoundException
    {
        return getPostfixName(new File(filename));
    }

    /**
     * 将文件长度按大小规格解析为字符串表示.
     * <p>
     * 如果大于等于1TB，以TB为单位返回整数, 并以GB为单位返回两位小数(当GB = 0时, 则只有一位小数: 0);<br>
     * 如果大于等于1GB，以GB为单位返回整数, 并以MB为单位返回两位小数(当MB = 0时, 则只有一位小数: 0);<br>
     * 如果大于等于1MB，以MB为单位返回整数, 并以KB为单位返回两位小数(当KB = 0时, 则只有一位小数: 0);<br>
     * 如果大小等于1KB，以KB为单位返回整数;<br>
     * 如果小于1KB，以字节为单位返回整数.<br>
     *
     * @param fileSize 目标文件的大小, 单位为字节.
     * @return 解析后的字符串.
     */
    public static String parseFileSize(long fileSize)
    {
        StringBuilder result = new StringBuilder();
        if (fileSize >= TB)
        {
            result.append(fileSize / TB).append(".")
                    .append((fileSize % TB) * 100 / TB).append("TB");
        }
        else if (fileSize >= GB)
        {
            result.append(fileSize / GB).append(".")
                    .append((fileSize % GB) * 100 / GB).append("GB");
        }
        else if (fileSize >= MB)
        {
            result.append(fileSize / MB).append(".")
                    .append((fileSize % MB) * 100 / MB).append("MB");
        }
        else if (fileSize >= 1024)
        {
            result.append(fileSize / 1024).append("KB");
        }
        else
        {
            result.append(fileSize).append("bytes");
        }

        return result.toString();
    }

    /**
     * 重命名文件或目录.
     * <p>
     * 本方法与java.io.File.renameTo()的区别在于: 不需要为两个文件对象都指定同一文件夹, 只要给出正确的文件名即可.
     * <p>
     * 另: 重命名文件时, 需自行保证不同平台的文件命名规则.
     *
     * @param file 要重新命名的文件或目录
     * @param name 新的文件名
     * @return 成功返回true, 失败返回false
     * @throws FileNotFoundException
     */
    public static boolean rename(File file, String name)
            throws FileNotFoundException
    {
        boolean isSuccessful = false;
        if (StringUtils.isNotEmpty(name))
        {
            if (file.getParent() == null)
            {
                File newname = new File(name);
                isSuccessful = file.renameTo(newname);
            }
            else
            {
                File newname = new File(file.getParentFile(), name);
                isSuccessful = file.renameTo(newname);
            }
        }

        return isSuccessful;
    }

    /**
     * 比较两个文件的内容是否相同.
     * <p>
     * 注意: 本方法是按字节一个个对比, 适用于比较任意两个8-bit文件(如:二进制文件、文本文件等).
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 如果相同返回true, 反之返回false
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean compare(File file1, File file2)
            throws FileNotFoundException, IOException
    {
        checkExistsAndIsFile(file1);
        checkExistsAndIsFile(file2);
        BufferedInputStream in_1 = null, in_2 = null;
        try
        {
            in_1 = new BufferedInputStream(new FileInputStream(file1));
            in_2 = new BufferedInputStream(new FileInputStream(file2));
            int len;
            // 逐个比较两个文件的字节是否相等, 如果不相等, 直接返回false.
            while ((len = in_1.read()) != -1)
            {
                if (len != in_2.read())
                    return false;
            }
            // 当文件1已读取完毕, 而文件2尚有数据, 证明不相等, 返回false.
            if (in_2.read() != -1)
                return false;

            return true;
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            try
            {
                if (in_1 != null)
                    in_1.close();
                if (in_2 != null)
                    in_2.close();
            }
            catch (IOException ex1)
            {
                // ignore
            }
        }
    }
    
    /**
     * 列出指定目录下的文件.
     * <p>
     * 注意: 本方法支持文件夹遍历, 将列出指定根目录下的所有内容. 如果需要按条件遍历, 请使用FileIterator.
     *
     * @see game.core.util.file.FileIterator
     * @param dir 要遍历的目录
     * @return 目录下的所有内容
     * @throws FileNotFoundException
     */
    public static File[] list(File dir) throws FileNotFoundException
    {
        checkExistsAndIsDirectory(dir);
        File[] files;
        final List<File> list = new ArrayList<>();
        /**
         * 递归执行目录遍历的内隐类
         */
        class ListIterator
        {

            public void list(File dir)
            {
                File[] listFiles = dir.listFiles();
                for (File file : listFiles)
                {
                    list.add(file);
                    // 如果当前抽象路径所表示的文件对象是目录, 则开始下一轮递归
                    if (file.isDirectory())
                        list(file);
                }
            }

        }

        ListIterator lt = new ListIterator();
        lt.list(dir);
        if (list.size() > 0)
        {
            files = new File[list.size()];
            list.toArray(files);
        }
        else
        {
            files = new File[0];
        }

        return files;
    }

    /**
     * 列出指定目录下的文件.
     * <p>
     * 注意: 本方法支持文件夹遍历, 将列出指定根目录下的所有内容. 如果需要按条件遍历, 请使用FileIterator.
     *
     * @see game.core.util.file.FileIterator
     * @param dirPath 要遍历的目录的全路径
     * @return 目录下的所有内容
     * @throws FileNotFoundException
     */
    public static File[] list(String dirPath) throws FileNotFoundException
    {
        return list(new File(dirPath));
    }

    /**
     * 清空指定目录下的文件.
     * <p>
     * 注意: 本方法只删除当前目录下的文件, 不会删除当前目录, 以及子目录和子目录下的文件.
     *
     * @param dir 要清空的目录
     * @return FileResultSet
     * @throws FileNotFoundException
     */
    public static FileResultSet clear(File dir) throws FileNotFoundException
    {
        checkExistsAndIsDirectory(dir);
        FileResultSet resultSet = new FileResultSet();
        int count = 0; // 统计成功删除的文件
        File[] listFiles = dir.listFiles();
        for (File file : listFiles)
        {
            // 当目录下还有文件存在的时候, 不能删除目录, 而本方法不支持遍历目录. 所以, 在删除前首先判断对象是否为目录.
            if (!file.isDirectory())
            {
                if (file.delete())
                    count++;
                else
                    resultSet.addFileFailed(file); // 删除失败, 将文件添加至失败队列
            }
        }
        resultSet.setTotalFiles(count);

        return resultSet;
    }

    /**
     * 清空指定目录下的文件.
     * <p>
     * 注意: 本方法只能删除当前目录下的文件, 不会删除当前目录, 以及子目录和子目录下的文件.
     *
     * @param dirPath 要清空的目录的全路径
     * @return FileResultSet
     * @throws FileNotFoundException
     */
    public static FileResultSet clear(String dirPath)
            throws FileNotFoundException
    {
        return clear(new File(dirPath));
    }

    /**
     * 删除指定目录及其下的所有内容.
     * <p>
     * 注意: 本方法支持文件夹遍历, 将在删除所有子目录及其下文件后, 随之删除当前目录.
     *
     * @param dir 要删除的目录
     * @return FileResultSet
     * @throws FileNotFoundException
     */
    public static FileResultSet delete(File dir) throws FileNotFoundException
    {
        checkExistsAndIsDirectory(dir);
        final FileResultSet resultSet = new FileResultSet();
        /**
         * 递归执行删除文件及目录的内隐类
         */
        class DeleteDir
        {

            private int countByFile = 0; // 统计成功删除的文件

            private int countByDirectory = 0; // 统计成功删除的目录

            public void execute(File dir)
            {
                File[] listFiles = dir.listFiles();
                for (File file : listFiles)
                {
                    // 如果当前抽象路径所表示的文件对象是目录, 则开始下一轮递归
                    if (file.isDirectory())
                    {
                        execute(file);
                    }
                    else
                    {
                        if (file.delete())
                            countByFile++;
                        else
                            resultSet.addFileFailed(file); // 删除失败, 将文件添加至失败队列
                    }
                }
                // 理论上, 目录下所有文件都删除后, 删除父目录
                if (dir.delete())
                    countByDirectory++;
                else
                    resultSet.addFileFailed(dir); // 删除失败, 将目录添加至失败队列
            }

        }

        DeleteDir del = new DeleteDir();
        del.execute(dir);
        resultSet.setTotalFiles(del.countByFile);
        resultSet.setTotalDirectory(del.countByDirectory);

        return resultSet;
    }

    /**
     * 删除指定目录及其下的所有内容.
     * <p>
     * 注意: 本方法支持文件夹遍历, 将在删除所有子目录及其下文件后, 随之删除当前目录.
     *
     * @param dirPath 要删除的目录的全路径
     * @return FileResultSet
     * @throws FileNotFoundException
     */
    public static FileResultSet delete(String dirPath)
            throws FileNotFoundException
    {
        return delete(new File(dirPath));
    }

    /**
     * 将目标文件拷贝到指定的输出目录下.
     * <p>
     * 注意: 本方法可以将文件拷贝到指定的目录下, 但不拷贝原文件的目录结构. 如果文件已存在, 将覆盖原文件.
     * <p>
     * 另: 重命名文件时, 需自行保证不同平台的文件命名规则.
     *
     * @param file 目标文件
     * @param outputDirPath 输出目录
     * @param rename 拷贝到新目录后重命名文件(包括后缀名)
     * @param bufferSize 设置缓冲区的大小(最小支持1KB. 单位: 字节)
     * @return 拷贝到新目录后的文件对象
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File copyFile(File file, String outputDirPath, String rename,
            int bufferSize) throws FileNotFoundException, IOException
    {
        checkExistsAndIsFile(file, " 目标文件不存在!", null);
        File dir = new File(outputDirPath);
        checkExistsAndIsDirectory(dir, " 输出目录不存在!", null);
        String filename;
        if (StringUtils.isEmpty(rename))
            filename = file.getName();
        else
            filename = rename;
        File outputFile = new File(dir, filename);
        // 考虑到不同数据类型的文件, 这里采用字节流工具类来读取并写入文件.
        BinaryFile.write(file, outputFile, bufferSize);

        return outputFile;
    }

    /**
     * 将目标文件拷贝到指定的输出目录下.
     * <p>
     * 注意: 本方法可以将文件拷贝到指定的目录下, 但不拷贝原文件的目录结构. 如果文件已存在, 将覆盖原文件.
     *
     * @param file 目标文件
     * @param outputDirPath 输出目录
     * @param bufferSize 设置缓冲区的大小(最小支持1KB. 单位: 字节)
     * @return 拷贝到新目录后的文件对象
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static File copyFile(File file, String outputDirPath, int bufferSize)
            throws FileNotFoundException, IOException
    {
        return copyFile(file, outputDirPath, null, bufferSize);
    }

    /**
     * 将输入目录(inputDir)以及其下所包含的所有文件或目录拷贝到输出目录(outputDir).
     * <p>
     * 注意: 本方法可以将目标目录以及其下所包含的所有文件或目录拷贝到指定的输出目录下, 同时保证inputDir的目录结构. 如果文件已存在,
     * 将覆盖原有文件.
     *
     * @param inputDir 输入目录
     * @param outputDir 输出目录
     * @param bufferSize 设置缓冲区的大小(最小支持1KB. 单位: 字节)
     * @return 此次操作共拷贝的文件总数(不包含目录数量!)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int copyDirectory(File inputDir, File outputDir,
            int bufferSize) throws FileNotFoundException, IOException
    {
        checkExistsAndIsDirectory(inputDir, " 输入目录不存在!", null);
        checkExistsAndIsDirectory(outputDir, " 输出目录不存在!", null);
        /**
         * 递归执行拷贝文件及目录的内隐类
         */
        class CopyDir
        {

            private int count = 0; // 统计拷贝的文件总数

            public void execute(File inputDir, File outputDir, int bufferSize)
                    throws IOException
            {
                outputDir.mkdirs(); // 创建子目录, 如果不存在的话
                File[] listFiles = inputDir.listFiles(); // 获取当前目录下的所有文件列表
                for (File file : listFiles)
                {
                    if (file.isFile())
                    {
                        File outputFile = new File(outputDir, file.getName());
                        // 考虑到不同数据类型的文件, 这里采用字节流工具类来读取并写入文件.
                        BinaryFile.write(file, outputFile, bufferSize);
                        count++;
                    }
                    else if (file.isDirectory())
                    {
                        // 如果当前抽象路径名所表示的文件对象是目录, 则开始下一轮递归
                        execute(file, new File(outputDir, file.getName()),
                                bufferSize);
                    }
                }
            }

        }

        CopyDir copy = new CopyDir();
        // 在outputDir下以inputDir的根目录为子地址, 重新创建文件对象, 以便内部类在outputDir下创建根目录.
        outputDir = new File(outputDir, inputDir.getName());
        copy.execute(inputDir, outputDir, bufferSize);

        return copy.count;
    }

    /**
     * 将输入目录(inputDir)以及其下所包含的所有文件或目录拷贝到输出目录(outputDir).
     * <p>
     * 注意: 本方法可以将目标目录以及其下所包含的所有文件或目录拷贝到指定的输出目录下, 同时保证inputDir的目录结构. 如果文件已存在,
     * 将覆盖原有文件.
     *
     * @param inputDirPath 输入目录的全路径
     * @param outputDirPath 输出目录的全路径
     * @param bufferSize 设置缓冲区的大小(最小支持1KB. 单位: 字节)
     * @return 此次操作共拷贝的文件总数(不包含目录数量!)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int copyDirectory(String inputDirPath, String outputDirPath,
            int bufferSize) throws FileNotFoundException, IOException
    {
        if (null == inputDirPath)
            throw new NullPointerException("inputDirPath");
        if (null == outputDirPath)
            throw new NullPointerException("outputDirPath");

        return copyDirectory(new File(inputDirPath), new File(outputDirPath),
                bufferSize);
    }

    /**
     * 将目标文件移动到指定的输出目录下(<b>WINDOWS上测试通过!</b>).
     * <p>
     * 注意: 本方法可以将目标文件移动到指定的输出目录下, 但不移动原文件的目录结构.
     * <p>
     * 另: 重命名文件时, 需自行保证不同平台的文件命名规则.
     *
     * @param file 目标文件
     * @param outputDirPath 输出目录的全路径
     * @param rename 输出到新目录后重命名文件(包括后缀名)
     * @return 移动成功后返回新文件的对象引用, 失败返回null
     * @throws FileNotFoundException
     */
    public static File moveFile(File file, String outputDirPath, String rename)
            throws FileNotFoundException
    {
        checkExistsAndIsFile(file, " 文件不存在!", null);
        File dir = new File(outputDirPath);
        checkExistsAndIsDirectory(dir, " 目录不存在!", null);

        String filename;
        if (StringUtils.isEmpty(rename))
            filename = file.getName();
        else
            filename = rename;

        File dest = new File(dir, filename);
        if (file.renameTo(dest))
        {
            return (dest);
        }

        return null;
    }

    /**
     * 将目标文件移动到指定的输出目录下(<b>WINDOWS上测试通过!</b>).
     * <p>
     * 注意: 本方法可以将目标文件移动到指定的输出目录下, 但不移动原文件的目录结构.
     * <p>
     * 另: 重命名文件时, 需自行保证不同平台的文件命名规则.
     *
     * @param file 目标文件
     * @param outputDirPath 输出目录的全路径
     * @return 移动成功后返回新文件的对象引用, 失败返回null
     * @throws FileNotFoundException
     */
    public static File moveFile(File file, String outputDirPath)
            throws FileNotFoundException
    {
        return moveFile(file, outputDirPath, null);
    }

    /**
     * 将输入目录(inputDir)以及其下所包含的所有文件或目录移动到输出目录(outputDir)(<b>WINDOWS上测试通过!</b>).
     * <p>
     * 注意: 本方法可以将目标目录以及其下所包含的所有文件或目录移动到指定的输出目录下, 同时保证inputDir的目录结构. 如果文件已存在,
     * 不能覆盖原文件, 将其作为失败文件写入队列.
     *
     * @param inputDir 输入目录
     * @param outputDir 输出目录
     * @return FileResultSet
     * @throws FileNotFoundException
     */
    public static FileResultSet moveDirectory(File inputDir, File outputDir)
            throws FileNotFoundException
    {
        checkExistsAndIsDirectory(inputDir, " 输入目录不存在!", null);
        checkExistsAndIsDirectory(outputDir, " 输出目录不存在!", null);
        final FileResultSet resultSet = new FileResultSet();
        /**
         * 递归执行移动文件及文件夹的内隐类
         */
        class MoveDir
        {

            private int countByFile = 0; // 统计移动的文件总数

            private int countByDirectory = 0; // 统计移动的目录总数

            public void cutAndPaste(File input, File outputDir)
            {
                outputDir.mkdirs(); // 创建子目录, 如果不存在的话
                File[] listFiles = input.listFiles(); // 获取当前目录下的所有文件列表
                for (File file : listFiles)
                {
                    if (file.isFile())
                    {
                        File dest = new File(outputDir, file.getName());
                        if (file.renameTo(dest))
                            countByFile++;
                        else
                            resultSet.addFileFailed(file); // 移动失败, 将文件添加至失败队列
                    }
                    else if (file.isDirectory())
                    {
                        // 如果当前抽象路径名所表示的文件对象是目录, 则开始下一轮递归
                        cutAndPaste(file, new File(outputDir, file.getName()));
                    }
                }
                // 理论上, 目录下所有文件都移动后, 删除父目录
                if (input.delete())
                    countByDirectory++;
                else
                    resultSet.addFileFailed(input); // 删除失败, 将目录添加至失败队列
            }

        }

        MoveDir move = new MoveDir();
        // 在outputDir下以inputDir的根目录为子地址, 重新创建文件对象, 以便内部类在outputDir下创建根目录.
        outputDir = new File(outputDir, inputDir.getName());
        move.cutAndPaste(inputDir, outputDir);
        resultSet.setTotalFiles(move.countByFile);
        resultSet.setTotalDirectory(move.countByDirectory);

        return resultSet;
    }

    /**
     * 将输入目录(inputDir)以及其下所包含的所有文件或目录移动到输出目录(outputDir)(<b>WINDOWS上测试通过!</b>).
     * <p>
     * 注意: 本方法可以将目标目录以及其下所包含的所有文件或目录移动到指定的输出目录下, 同时保证inputDir的目录结构. 如果文件已存在,
     * 不能覆盖原文件, 将其作为失败文件写入队列.
     *
     * @param inputDirPath 输入目录的全路径
     * @param outputDirPath 输出目录的全路径
     * @return FileResultSet
     * @throws FileNotFoundException
     */
    public static FileResultSet moveDirectory(String inputDirPath,
            String outputDirPath) throws FileNotFoundException, IOException
    {
        if (null == inputDirPath)
            throw new NullPointerException("inputDirPath");
        if (null == outputDirPath)
            throw new NullPointerException("outputDirPath");

        return moveDirectory(new File(inputDirPath), new File(outputDirPath));
    }

    /**
     * 检查File对象的抽象路径名所表示的文件或目录是否存在, 以及是否是一个标准文件.
     *
     * @param file 文件对象
     * @param desc1 当对象不存在的描述信息, 如果为null, 采用默认的描述信息.
     * @param desc2 当对象不是一个标准文件的描述信息, 如果为null, 采用默认的描述信息.
     * @throws FileNotFoundException 如果不存在或者不是一个标准文件时抛出简单的错误描述
     */
    static void checkExistsAndIsFile(File file, String desc1, String desc2)
            throws FileNotFoundException
    {
        if (null == file || !file.exists())
        {
            if (null == desc1)
                throw new FileNotFoundException(file + " 不存在!");
            else
                throw new FileNotFoundException(file + desc1);
        }
        else if (!file.isFile())
        {
            if (null == desc2)
                throw new FileNotFoundException(file + " 不是一个文件!");
            else
                throw new FileNotFoundException(file + desc2);
        }
    }

    /**
     * 检查File对象的抽象路径名所表示的文件或目录是否存在, 以及是否是一个标准文件.
     *
     * @param file 文件对象
     * @throws FileNotFoundException 如果不存在或者不是一个标准文件时抛出简单的错误描述
     */
    static void checkExistsAndIsFile(File file) throws FileNotFoundException
    {
        checkExistsAndIsFile(file, null, null);
    }

    /**
     * 检查File对象的抽象路径名所表示的文件或目录是否存在, 以及是否是一个目录.
     *
     * @param file 文件对象
     * @param desc1 当对象不存在的描述信息, 如果为null, 采用默认的描述信息.
     * @param desc2 当对象不是一个目录的描述信息, 如果为null, 采用默认的描述信息.
     * @throws FileNotFoundException 如果不存在或者不是一个目录时抛出简单的错误描述
     */
    static void checkExistsAndIsDirectory(File file, String desc1, String desc2)
            throws FileNotFoundException
    {
        if (null == file || !file.exists())
        {
            if (null == desc1)
                throw new FileNotFoundException(file + " 不存在!");
            else
                throw new FileNotFoundException(file + desc1);
        }
        else if (!file.isDirectory())
        {
            if (null == desc2)
                throw new FileNotFoundException(file + " 不是一个目录!");
            else
                throw new FileNotFoundException(file + desc2);
        }
    }

    /**
     * 检查File对象的抽象路径名所表示的文件或目录是否存在, 以及是否是一个目录.
     *
     * @param file 文件对象
     * @throws FileNotFoundException 如果不存在或者不是一个目录时抛出简单的错误描述
     */
    static void checkExistsAndIsDirectory(File file)
            throws FileNotFoundException
    {
        checkExistsAndIsDirectory(file, null, null);
    }

}

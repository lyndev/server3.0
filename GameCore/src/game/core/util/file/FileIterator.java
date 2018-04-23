package game.core.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

import game.core.util.StringUtils;

/**
 *
 * <b>文件遍历器.</b>
 * <p>
 * 用于搜索文件, 从某根文件夹开始, 递归遍历它的所有子目录, 返回满足查询条件的文件列表(当不熟悉正则表达式时, 本类提供了另一种途径).
 * <p>
 * <b>Sample:</b>
 *
 * <pre>
 * FileIterator fit = new FileIterator();
 * // 设置允许的文件类型
 * fit.setAllowedFileTypes(&quot;class, java&quot;);
 * // 设置禁止的文件类型
 * fit.setDeniedFileTypes(&quot;class&quot;);
 * // 按上述规则查找指定目录
 * File[] listFiles = fit.search(&quot;C:\\tomcat-5.5.12&quot;);
 * for (int i = 0; i &lt; listFiles.length; i++) {
 * 	System.out.println(list[i]);
 * }
 * System.out.println(&quot;total: &quot; + list.length);
 *
 * // 重新设置允许的文件类型
 * fit.setAllowedFileTypes(&quot;html&quot;);
 * // 按新规则执行查询
 * for (int i = 0; i &lt; listFiles.length; i++) {
 * 	System.out.println(list[i]);
 * }
 * System.out.println(&quot;total: &quot; + list.size());
 * </pre>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public class FileIterator
{

    /**
     * 允许的文件名称
     */
    private List<String> allowedFileNames;

    /**
     * 允许的文件类型
     */
    private List<String> allowedFileTypes;

    /**
     * 禁止的文件名称
     */
    private List<String> deniedFileNames;

    /**
     * 禁止的文件类型
     */
    private List<String> deniedFileTypes;

    /**
     * 设置查询条件: 允许的文件名称.
     * <p>
     * 注意: 检查匹配时DeniedFileNames的优先级高于AllowedFileNames; 另外, 重复调用会先清空此前的设置!
     *
     * @see #setDeniedFileNames(String)
     * @param fileNames 名称列表, 多个名称以逗号相隔. 如: file1.txt, file2.doc, file3.xls
     */
    public void setAllowedFileNames(String fileNames)
    {
        if (StringUtils.isNotEmpty(fileNames))
        {
            if (null == allowedFileNames)
                allowedFileNames = new ArrayList<>();
            else
                allowedFileNames.clear();

            String ext = "";
            int length = fileNames.length();
            for (int i = 0; i < length; i++)
            {
                if (fileNames.charAt(i) == ',')
                {
                    if (!allowedFileNames.contains(ext))
                        allowedFileNames.add(ext.trim());
                    ext = "";
                }
                else
                {
                    ext = ext + fileNames.charAt(i);
                }
            }

            if (!"".equals(ext))
                allowedFileNames.add(ext.trim());
        }
    }

    /**
     * 设置查询条件: 允许的文件类型.
     * <p>
     * 注意: 检查匹配时DeniedFileTypes的优先级高于AllowedFileTypes; 另外, 重复调用会先清空此前的设置!
     *
     * @see #setDeniedFileTypes(String)
     * @param fileTypes 类型列表, 多个类型以逗号相隔. 如: txt, doc, xls
     */
    public void setAllowedFileTypes(String fileTypes)
    {
        if (StringUtils.isNotEmpty(fileTypes))
        {
            if (null == allowedFileTypes)
                allowedFileTypes = new ArrayList<>();
            else
                allowedFileTypes.clear();

            String ext = "";
            int length = fileTypes.length();
            for (int i = 0; i < length; i++)
            {
                if (fileTypes.charAt(i) == ',')
                {
                    if (!allowedFileTypes.contains(ext))
                        allowedFileTypes.add(ext.trim());
                    ext = "";
                }
                else
                {
                    ext = ext + fileTypes.charAt(i);
                }
            }

            if (!"".equals(ext))
                allowedFileTypes.add(ext.trim());
        }
    }

    /**
     * 设置查询条件: 禁止的文件名称.
     * <p>
     * 注意: 检查匹配时DeniedFileNames的优先级高于AllowedFileNames; 另外, 重复调用会先清空此前的设置!
     *
     * @param fileNames 名称列表, 多个名称以逗号相隔. 如: file1, file2, file3
     */
    public void setDeniedFileNames(String fileNames)
    {
        if (StringUtils.isNotEmpty(fileNames))
        {
            if (null == deniedFileNames)
                deniedFileNames = new ArrayList<>();
            else
                deniedFileNames.clear();

            String ext = "";
            int length = fileNames.length();
            for (int i = 0; i < length; i++)
            {
                if (fileNames.charAt(i) == ',')
                {
                    if (!deniedFileNames.contains(ext))
                        deniedFileNames.add(ext.trim());
                    ext = "";
                }
                else
                {
                    ext = ext + fileNames.charAt(i);
                }
            }

            if (!"".equals(ext))
                deniedFileNames.add(ext.trim());
        }
    }

    /**
     * 设置查询条件: 禁止的文件类型.
     * <p>
     * 注意: 检查匹配时DeniedFileTypes的优先级高于AllowedFileTypes; 另外, 重复调用会先清空此前的设置!
     *
     * @param fileTypes 类型列表, 多个类型以逗号相隔. 如: txt, doc, xls
     */
    public void setDeniedFileTypes(String fileTypes)
    {
        if (StringUtils.isNotEmpty(fileTypes))
        {
            if (null == deniedFileTypes)
                deniedFileTypes = new ArrayList<>();
            else
                deniedFileTypes.clear();

            String ext = "";
            int length = fileTypes.length();
            for (int i = 0; i < length; i++)
            {
                if (fileTypes.charAt(i) == ',')
                {
                    if (!deniedFileTypes.contains(ext))
                        deniedFileTypes.add(ext.trim());
                    ext = "";
                }
                else
                {
                    ext = ext + fileTypes.charAt(i);
                }
            }

            if (!"".equals(ext))
                deniedFileTypes.add(ext.trim());
        }
    }

    /**
     * 执行遍历, 查询条件是通过setAllowedFileNames等方法设置的, 如未作任何设置则匹配所有文件.
     *
     * @param dir 执行遍历的根目录
     * @return 满足查询条件的文件
     * @throws FileNotFoundException
     */
    public File[] search(File dir) throws FileNotFoundException
    {
        FileUtils.checkExistsAndIsDirectory(dir);
        File[] files;
        final List<File> temp = new ArrayList<>();
        /**
         * 递归执行目录遍历的内隐类
         */
        class ListIterator
        {

            public void list(File dir) throws FileNotFoundException
            {
                File[] listFiles = dir.listFiles();
                for (File file : listFiles)
                {
                    if (file.isFile())
                    {
                        String fileName = file.getName();
                        // 获取文件扩展名
                        String fileType = FileUtils.getPostfixName(file);
                        // 检查禁止的属性
                        if (null != deniedFileNames || null != deniedFileTypes)
                        {
                            if (checkDeniedAttribute(fileName, fileType))
                                continue;
                        }
                        // 匹配允许的属性
                        if (null != allowedFileNames
                                || null != allowedFileTypes)
                        {
                            if (!checkAllowedAttribute(fileName, fileType))
                                continue;
                        }
                        // 该文件符合查询条件, 将其添加至列表
                        temp.add(file);
                    }
                    else if (file.isDirectory())
                    {
                        // 如果当前抽象路径所表示的文件对象是目录, 则开始下一轮递归
                        list(file);
                    }
                }
            }

        }

        ListIterator lt = new ListIterator();
        lt.list(dir);
        if (temp.size() > 0)
        {
            files = new File[temp.size()];
            temp.toArray(files);
        }
        else
        {
            files = new File[0];
        }

        return files;
    }

    /**
     * 执行遍历, 查询条件是通过setAllowedFileNames等方法设置的, 如未作任何设置则匹配所有文件.
     *
     * @param dirPath 执行遍历的根目录
     * @return 满足查询条件的文件
     * @throws FileNotFoundException
     */
    public File[] search(String dirPath) throws FileNotFoundException
    {
        return search(new File(dirPath));
    }

    /**
     * 执行遍历, 以目标文件名为查询条件.
     *
     * @param dir 执行遍历的根目录
     * @param filename 目标文件名
     * @return 满足查询条件的文件
     * @throws FileNotFoundException
     */
    public File[] searchByName(File dir, String filename)
            throws FileNotFoundException
    {
        FileUtils.checkExistsAndIsDirectory(dir);
        File[] files;
        final String name = filename;
        final List<File> temp = new ArrayList<>();
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
                    if (file.isFile())
                    {
                        // 匹配文件名, 如果相等, 则添加至列表
                        if (name.equalsIgnoreCase(file.getName()))
                            temp.add(file);
                    }
                    else if (file.isDirectory())
                    {
                        // 如果当前抽象路径所表示的文件对象是目录, 则开始下一轮递归
                        list(file);
                    }
                }
            }

        }

        ListIterator lt = new ListIterator();
        lt.list(dir);
        if (temp.size() > 0)
        {
            files = new File[temp.size()];
            temp.toArray(files);
        }
        else
        {
            files = new File[0];
        }

        return files;
    }

    /**
     * 执行遍历, 以目标文件名为查询条件.
     *
     * @param dirPath 执行遍历的根目录
     * @param filename 目标文件名
     * @return 满足查询条件的文件
     * @throws FileNotFoundException
     */
    public File[] searchByName(String dirPath, String filename)
            throws FileNotFoundException
    {
        return searchByName(new File(dirPath), filename);
    }

    /**
     * 执行遍历, 以局部文件名为查询条件, 即文件名只要包含局部文件名则满足条件.
     *
     * @param dir 执行遍历的根目录
     * @param partname 局部文件名
     * @return 满足查询条件的文件
     * @throws FileNotFoundException
     */
    public File[] searchByPartName(File dir, String partname)
            throws FileNotFoundException
    {
        FileUtils.checkExistsAndIsDirectory(dir);
        File[] files;
        final String name = partname;
        final List<File> temp = new ArrayList<>();
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
                    if (file.isFile())
                    {
                        // 匹配局部文件名, 如果文件名中包含局部文件名(partname), 则添加至列表
                        if (StringUtils
                                .containsIgnoreCase(file.getName(), name))
                            temp.add(file);
                    }
                    else if (file.isDirectory())
                    {
                        // 如果当前抽象路径所表示的文件对象是目录, 则开始下一轮递归
                        list(file);
                    }
                }
            }

        }

        ListIterator lt = new ListIterator();
        lt.list(dir);
        if (temp.size() > 0)
        {
            files = new File[temp.size()];
            temp.toArray(files);
        }
        else
        {
            files = new File[0];
        }

        return files;
    }

    /**
     * 执行遍历, 以局部文件名为查询条件, 即文件名只要包含局部文件名则满足条件(忽略大小写检查).
     *
     * @param dirPath 执行遍历的根目录
     * @param partname 局部文件名
     * @return 满足查询条件的文件
     * @throws FileNotFoundException
     */
    public File[] searchByPartName(String dirPath, String partname)
            throws FileNotFoundException
    {
        return searchByPartName(new File(dirPath), partname);
    }

    /**
     * 执行遍历, 不作条件查询, 返回根目录下的所有对象.
     *
     * @param dir 执行遍历的根目录
     * @return 装载了所有文件和目录对象的二维数组, 其中, 索引0存放目录数组, 索引1存放文件数组.
     * @throws FileNotFoundException
     */
    public File[][] list(File dir) throws FileNotFoundException
    {
        FileUtils.checkExistsAndIsDirectory(dir);
        File[][] results = new File[2][];
        final List<File> dirList = new ArrayList<>();
        final List<File> fileList = new ArrayList<>();
        /**
         * 递归执行目录遍历的内隐类
         */
        class ListIterator
        {

            public void list(File dir) throws FileNotFoundException
            {
                File[] listFiles = dir.listFiles();
                for (File file : listFiles)
                {
                    if (file.isFile())
                    {
                        fileList.add(file);
                    }
                    else if (file.isDirectory())
                    {
                        dirList.add(file);
                        list(file);
                    }
                }
            }

        }

        ListIterator lt = new ListIterator();
        lt.list(dir);
        if (dirList.size() > 0)
        {
            results[0] = new File[dirList.size()];
            dirList.toArray(results[0]);
        }
        else
        {
            results[0] = new File[0];
        }
        if (fileList.size() > 0)
        {
            results[1] = new File[fileList.size()];
            fileList.toArray(results[1]);
        }
        else
        {
            results[1] = new File[0];
        }

        return results;
    }

    private boolean checkDeniedAttribute(String fileName, String fileType)
    {
        if (null != deniedFileNames)
        {
            for (String deniedName : deniedFileNames)
            {
                if (fileName.equalsIgnoreCase(deniedName))
                    return true;
            }
        }

        if (null != deniedFileTypes)
        {
            for (String deniedType : deniedFileTypes)
            {
                if (fileType.equalsIgnoreCase(deniedType))
                    return true;
            }
        }

        return false;
    }

    private boolean checkAllowedAttribute(String fileName, String fileType)
    {
        if (null != allowedFileNames)
        {
            for (String allowedName : allowedFileNames)
            {
                if (fileName.equalsIgnoreCase(allowedName))
                    return true;
            }
        }

        if (null != allowedFileTypes)
        {
            for (String allowedType : allowedFileTypes)
            {
                if (fileType.equalsIgnoreCase(allowedType))
                    return true;
            }
        }

        return false;
    }

}

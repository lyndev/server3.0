/**
 * @date 2014/5/8
 * @author ChenLong
 */
package game.server.util;

import game.core.exception.UninitializedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * <b>敏感词判断/过滤工具类</b>
 *
 * @author ChenLong
 */
public class SensitiveWordFilter
{
    private final Logger log = Logger.getLogger(SensitiveWordFilter.class);
    private final List<String> allSensitiveWords = new ArrayList<>(); // 所有敏感词
    private final WordNode rootWordNode = new WordNode('R');
    private boolean isInitialized = false;

    /**
     * 初始化敏感词集
     *
     * @param allSensitiveWords
     */
    public synchronized void initSensitiveWords(List<String> allSensitiveWords)
    {
        log.info("initialize SensitiveWordFilter");
        this.allSensitiveWords.clear();
        this.allSensitiveWords.addAll(allSensitiveWords);
        buildWordTree();
        isInitialized = true;
    }

    /**
     * 是否有敏感词
     *
     * @param content
     * @return
     */
    public synchronized boolean hasSensitiveWord(String content)
    {
        if (!isInitialized)
            throw new UninitializedException("SensitiveWordFilter has not initialized");
        char[] chars = content.toCharArray();
        WordNode node = rootWordNode;
        StringBuilder buffer = new StringBuilder();
        List<String> word = new ArrayList<>();
        int a = 0;
        while (a < chars.length)
        {
            node = findNode(node, chars[a]);
            if (node == null)
            {
                node = rootWordNode;
                a = a - word.size();
                buffer.append(chars[a]);
                word.clear();
            }
            else if (node.endFlag == 1)
            {
                node = null;
                return true;
            }
            else
            {
                word.add(String.valueOf(chars[a]));
            }
            a++;
        }
        chars = null; // help GC ?
        word.clear();
        word = null; // help GC ?
        return false;
    }

    /**
     * 过滤敏感词
     *
     * @param content
     * @return
     */
    public synchronized String filterSensitiveWord(String content)
    {
        if (!isInitialized)
            throw new UninitializedException("SensitiveWordFilter has not initialized");
        char[] chars = content.toCharArray();
        WordNode node = rootWordNode;
        StringBuilder buffer = new StringBuilder();
        List<String> badList = new ArrayList<>();
        int a = 0;
        while (a < chars.length)
        {
            node = findNode(node, chars[a]);
            if (node == null)
            {
                node = rootWordNode;
                a = a - badList.size();
                if (badList.size() > 0)
                {
                    badList.clear();
                }
                buffer.append(chars[a]);
            }
            else if (node.endFlag == 1)
            {
                badList.add(String.valueOf(chars[a]));
                for (String badList1 : badList)
                {
                    buffer.append("*");
                }
                node = rootWordNode;
                badList.clear();
            }
            else
            {
                badList.add(String.valueOf(chars[a]));
                if (a == chars.length - 1)
                {
                    for (String badList1 : badList)
                    {
                        buffer.append(badList1);
                    }
                }
            }
            a++;
        }
        return buffer.toString();
    }

    private void buildWordTree()
    {
        rootWordNode.reset('R');
        for (String str : allSensitiveWords)
        {
            char[] chars = str.toCharArray();
            if (chars.length > 0)
                insertNode(rootWordNode, chars, 0);
        }
    }

    private void insertNode(WordNode node, char[] cs, int index)
    {
        WordNode n = findNode(node, cs[index]);
        if (n == null)
        {
            n = new WordNode(cs[index]);
            node.wordNodes.put(String.valueOf(cs[index]), n);
        }

        if (index == (cs.length - 1))
            n.endFlag = 1;

        index++;
        if (index < cs.length)
            insertNode(n, cs, index);
    }

    private WordNode findNode(WordNode node, char word)
    {
        return node.wordNodes.get(String.valueOf(word));
    }

    private static class WordNode
    {
        public char word;
        public int endFlag;
        public HashMap<String, WordNode> wordNodes = new HashMap<>();

        public WordNode(char word)
        {
            reset(word);
        }

        public final void reset(char word)
        {
            this.word = word;
            this.endFlag = 0;
            wordNodes.clear();
        }
    }

    public static SensitiveWordFilter getInstance()
    {
        return Singleton.INSTANCE.getService();
    }

    private enum Singleton
    {
        INSTANCE;
        SensitiveWordFilter filter;

        Singleton()
        {
            this.filter = new SensitiveWordFilter();
        }

        SensitiveWordFilter getService()
        {
            return filter;
        }
    }
}

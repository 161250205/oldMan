import java.text.Collator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Editor extends Worker {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final char[] PUNCTUATIONS = {'.',',','!','?','"','。','，','！','？','”'};
    private static final int MAX_SIZE = 32;
    public Editor() {
        super();
    }

    //初始化Editor
    public Editor(String name, int age, int salary) {
        super(name,age,salary,"Editor");
    }

    /**
     * 文本对齐
     * <p>
     * 根据统计经验，用户在手机上阅读英文新闻的时候，
     * 一行最多显示32个字节（1个中文占2个字节）时最适合用户阅读。显示16个中文
     * 给定一段字符串，重新排版，使得每行恰好有32个字节，并输出至控制台
     * 首行缩进4个字节，其余行数左对齐，每个短句不超过32个字节，
     * 每行最后一个有效字符必须为标点符号
     * <p>
     * 示例：
     * <p>
     * String：给定一段字符串，重新排版，使得每行恰好有32个字符，并输出至控制台首行缩进，其余行数左对齐，
     * 每个短句不超过32个字符。
     * <p>
     * 控制台输出:
     * 给定一段字符串，重新排版，
     * 使得每行恰好有32个字符，
     * 并输出至控制台首行缩进，
     * 其余行数左对齐，
     * 每个短句不超过32个字符。
     */
    public void textExtraction(String data) {
        List<String> sentences = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();
        String sentence = "";
        for (int i = 0; i < data.length(); i++) {
            char ch  = data.charAt(i);
            //判断是否是标点符号
            if (isPunctuation(ch)){
                sentence += ch;
                sentences.add(sentence);
                int size = sizeOfSentence(sentence);
                sizes.add(size);
                sentence = "";
            }else sentence += ch;
        }

        StringBuilder format = new StringBuilder("    ");
        int count = 4;
        int i = 0;
        while (i < sentences.size()){
            //如果等于32个字节换行
            if (count == 32){
                format.append(NEW_LINE);
                count = 0;
            }
            //如果大于32个字节说明不能加到一行，需要进行换行
            if (count + sizes.get(i) >  MAX_SIZE){
                count = 0;
                format.append(NEW_LINE);
            }else {
                format.append(sentences.get(i));
                count += sizes.get(i++);
            }
        }

        //打印格式化后的字符串
        System.out.println(format);
    }

    /**
     * 计算一个句子的字节数
     * @param sentence
     * @return
     */
    private int sizeOfSentence(String sentence){
        int n = sentence.length();
        int count = 0;
        for (int i = 0; i < n; i++) {
            char ch = sentence.charAt(i);
            if (isCharacter(String.valueOf(ch)))
                count+=2;
            else count +=1;
        }

        return count;
    }

    /**
     * 判断一个字符是不是汉字
     *
     * @param character
     * @return
     */
    public boolean isCharacter(String character) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(character);
        boolean flg = false;
        if (matcher.find()) flg = true;
        return flg;
    }

    /**
     * 判断一个字符是不是标点符号
     * @param ch
     * @return
     */
    private boolean isPunctuation(char ch) {
        for (char PUNCTUATION : PUNCTUATIONS) {
            if (ch == PUNCTUATION) return true;
        }
        return false;
    }


    /**
     * 标题排序
     * <p>
     * 将给定的新闻标题按照拼音首字母进行排序，
     * 首字母相同则按第二个字母，如果首字母相同，则首字拼音没有后续的首字排在前面，如  鹅(e)与二(er)，
     * 以鹅为开头的新闻排在以二为开头的新闻前。
     * 如果新闻标题第一个字的拼音完全相同，则按照后续单词进行排序。如 新闻1为 第一次...  新闻2为 第二次...，
     * 则新闻2应该排在新闻1之前。
     * 示例：
     * <p>
     * newsList：我是谁；谁是我；我是我
     * <p>
     * return：谁是我；我是谁；我是我；
     *
     * @param newsList
     */
    public ArrayList<String> newsSort(ArrayList<String> newsList) {
        Collator c = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
        Collections.sort(newsList,c);
        return newsList;

    }


    /**
     * 热词搜索
     * <p>
     * 根据给定的新闻内容，找到文中出现频次最高的一个词语，词语长度最少为2（即4个字节），最多为10（即20个字节），且词语中不包含标点符号，可以出现英文，同频词语选择在文中更早出现的词语。
     * <p>
     * 示例：
     * <p>
     * String: 今天的中国，呈现给世界的不仅有波澜壮阔的改革发展图景，更有一以贯之的平安祥和稳定。这平安祥和稳定的背后，凝聚着中国治国理政的卓越智慧，也凝结着中国公安民警的辛勤奉献。
     * <p>
     * return：中国
     *
     * @param newsContent
     */
    public String findHotWords(String newsContent) {
        String tmp;

        ArrayList<String> wordList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        //将所给字符串的标点符号替换为空格，方便进一步处理
        tmp = newsContent.replaceAll("[\\p{Punct}\\pP]", " ");
        //计算每个单词的出现数量
        setHotWordsList(wordList,countList,tmp);

        int largePos = 0;
        int largeNum = 0;
        //找到出现次数最多、最长、最先出现的字符串
        for (int i = 0; i < wordList.size(); i++) {
            if(countList.get(i) > largeNum ||
                    (countList.get(i) == largeNum &&
                            newsContent.indexOf(wordList.get(largePos)) == newsContent.indexOf(wordList.get(i)))){
                largePos = i;
                largeNum = countList.get(i);
            }

        }

        return wordList.get(largePos);
    }

    /**
     * 对传入的经过预处理的字符串进行分析，计算出所有单词的数量
     *
     * @param wordList，countList, tmp
     */
    private void setHotWordsList(ArrayList<String> wordList,ArrayList<Integer> countList,String tmp){
        for(int i=0;i<tmp.length()-1;i++){
            if(tmp.charAt(i) == ' ' ||( tmp.charAt(i+1) == ' '))continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(tmp.charAt(i));
            //从第一个字符开始，计算以该字符开头的所有单词
            for (int j = 0;j < 9;j++){

                if(j+i+1 >= tmp.length() || tmp.charAt(j+i+1) == ' ' )break;
                stringBuilder.append(tmp.charAt(j+i+1));
                //如果之前存在相同的字符串，则数量加一
                if(wordList.contains(stringBuilder.toString())){
                    int pos = wordList.indexOf(stringBuilder.toString());
                    int count;
                    count = countList.get(pos);
                    count++;
                    countList.set(pos,count);
                }
                //否则新建一个新的
                else {
                    wordList.add(stringBuilder.toString());
                    countList.add(1);

                }
            }
        }
    }

    /**
     * 相似度对比
     * <p>
     * 为了检测新闻标题之间的相似度，公司需要一个评估字符串相似度的算法。
     * 即一个新闻标题A修改到新闻标题B需要几步操作，我们将最少需要的次数定义为 最少操作数
     * 操作包括三种： 替换：一个字符替换成为另一个字符，
     * 插入：在某位置插入一个字符，
     * 删除：删除某位置的字符
     * 示例：
     * 中国队是冠军  -> 我们是冠军
     * 最少需要三步来完成。第一步删除第一个字符  "中"
     * 第二步替换第二个字符  "国"->"我"
     * 第三步替换第三个字符  "队"->"们"
     * 因此 最少的操作步数就是 3
     * <p>
     * 定义相似度= 1 - 最少操作次数/较长字符串的长度
     * 如在上例中：相似度为  (1 - 3/6) * 100= 50.00（结果保留2位小数，四舍五入，范围在0.00-100.00之间）
     *
     * @param title1
     * @param title2
     */
    public double minDistance(String title1, String title2) {
        char[] s = title1.toCharArray();
        char[] t = title2.toCharArray();
        int m = s.length;
        int n = t.length;
        int maxLength = Math.max(m,n);
        int[][] dp = new int[m + 1][n + 1];
        //得到最小的操作数
        int min = minDistance(s, t, dp, m, n);
        //计算相似度
        double res = (1-(double)min/maxLength) * 100;

        return Double.parseDouble(String.format("%.2f", res));
    }

    private int minDistance(char[] s, char[] t, int[][] dp, int i, int j) {
        if (dp[i][j] != 0) return dp[i][j];
        if (i == 0) return dp[0][j] = j;
        if (j == 0) return dp[i][0] = i;
        if (s[i - 1] == t[j - 1]) return dp[i][j] = minDistance(s, t, dp, i - 1, j - 1);

        return dp[i][j] = Math.min(minDistance(s, t, dp, i - 1, j),
                Math.min(minDistance(s, t, dp, i, j - 1),
                        minDistance(s, t, dp, i - 1, j - 1))) + 1;
    }
}

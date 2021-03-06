import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programmer extends Worker {
    public String language;
    public String type;

    public Programmer() {
    }

    // Programmer类的初始化
    public Programmer(String name, int age, int salary, String language,
                      String type) {
        super(name, age, salary, "");
        this.language = language;
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // 按照规则计算当月的奖金
    public String getBonus(int overtime) {
        if(overtime<0)throw new IllegalArgumentException("Overtime illegal!");
        double bonus = 0;
        double other = 0;
        switch (this.getType())

        {
            case "Develop":
                bonus += this.getSalary() * 0.2;
                other = overtime * 100.0;
                other = other > 500 ? 500 : other;
                bonus += other;

                break;
            case "UI":
                bonus = this.getSalary() * 0.25;
                other = overtime * 50.0;
                other = other > 300 ? 300 : other;
                bonus += other;

                break;
            case "Test":
                bonus = this.getSalary() * 0.15;
                other = overtime * 150.0;
                other = other > 1000 ? 1000 : other;
                bonus += other;
                break;
            default:
                break;
        }
        return String.format("%,.2f", bonus);
    }

    // 展示基本信息
    @Override
    public String show() {
        StringBuilder infoBuffer = new StringBuilder();
        infoBuffer.append("My name is ");
        infoBuffer.append(this.getName());
        infoBuffer.append(" ; age : ");
        infoBuffer.append(this.getAge());
        infoBuffer.append(" ; language : ");
        infoBuffer.append(this.getLanguage());
        infoBuffer.append(" ; salary : ");
        infoBuffer.append(this.getSalary());
        infoBuffer.append(".");
        return infoBuffer.toString();
    }

    /**
     * 信息隐藏
     *
     * 为了保护用户的隐私，系统需要将用户号码和邮箱隐藏起来。 对输入的邮箱或电话号码进行加密。 commnet
     * 可能是一个邮箱地址，也可能是一个电话号码。
     *
     * 1. 电子邮箱 邮箱格式为 str1@str2
     * 电子邮箱的名称str1是一个长度大于2.并且仅仅包含大小写字母和数字的字符串，名称str1后紧接符号@
     * 最后是邮箱所在的服务器str2,str2中可能包含多个. 如qq.com smail.nju.edu.cn等 邮箱地址是有效的，一个正确的示例为:
     * str1@smail.nju.edu.cn 为了隐藏电子邮箱，所有的str1和str2中的字母必须被转换成小写的，
     * 并且名称str1的第一个字和最后一个字的中间的所有字由 5 个 '*' 代替。 如果邮箱中含有非法字符或格式不正确，则返回illegal
     *
     * 示例：
     *
     * comment: "Qm@Qq.com"
     *
     * return: "q*****m@qq.com"
     *
     * 2. 电话号码 电话号码是一串包括数字 0-9，以及 {'+', '-', '(', ')', ' '} 这几个字符的字符串。
     * 你可以假设电话号码包含 10 到 13 个数字。 电话号码的最后 10 个数字组成本地号码，在这之前的数字组成国际号码。
     * 国际号码是可选的。我们只暴露最后 4 个数字并隐藏所有其他数字。 本地号码有格式，并且如 "***-***-1111" 这样显示，
     * 为了隐藏有国际号码的电话号码，像 "+111 111 111 1111"，我们以 "+***-***-***-1111"
     * 的格式来显示。在本地号码前面的 '+' 号 和第一个 '-' 号仅当电话号码中包含国际号码时存在。 例如，一个 12 位的电话号码应当以
     * "+**-" 开头进行显示。 注意：像 "("，")"，" " 这样的不相干的字符以及不符合上述格式的额外的减号或者加号都应当被删除。 示例1:
     *
     * comment: "1(234)567-890"
     *
     * return: "***-***-7890"
     *
     * 示例2:
     *
     * comment: "86-(10)12345678"
     *
     * return: "+**-***-***-5678"
     *
     * @param comment
     */
    public String hideUserinfo(String comment) {

        comment=comment.toLowerCase();
        String checkEmail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(checkEmail);
        Matcher matcher = regex.matcher(comment);
        boolean isEmail = matcher.matches();
        if (isEmail) {
            String[] list = comment.split("@");
            String str1 = list[0];
            String str2 = list[1];
            return str1.substring(0, 1) + "*****"
                    + str1.substring(str1.length() - 1, str1.length()) + "@"
                    + str2;
        } else {
            comment=comment.replaceAll(" ", "");
            comment=comment.replaceAll("[()\\+-]", "");
            String checkNumberString="[0-9]+";
            regex=Pattern.compile(checkNumberString);
            matcher=regex.matcher(comment);
            boolean isNumber=matcher.matches();
            if(isNumber){
                if(comment.length()==10){
                    return "***-***-"+comment.substring(6, comment.length());
                }else if(comment.length()>10&&comment.length()<14){
                    String internationString=comment.substring(0,comment.length()-10);
                    String starsString="********************";
                    internationString="+"+starsString.substring(0,internationString.length());
                    return internationString+"-***-***-"+comment.substring(comment.length()-4,comment.length());
                }else{
                    throw new IllegalArgumentException();
                }
            }else{
                throw new IllegalArgumentException();
            }

        }
    }

}

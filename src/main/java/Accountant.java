
public class Accountant extends Worker {
	private String password;
	private String[] units = {"Billion","Million","Thousand","Hundred"};
	private String[] belowtens = {"One","Two","Three","Four","Five","Six","Seven","Eight","Nine"};
	private String[] tens = {"Ten","Eleven","Twelve","Thirteen","Fourteen","Fifteen","Sixteen","Seventeen","Eighteen","Nineteen"};
	private String[] overtens = {"Twenty","Thirty","Forty","Fifty","Sixty","Seventy","Eighty","Ninety"};
	public Accountant() {

	}
	
	//初始化Accountant
	public Accountant(String name, int age, int salary, String password) {
			super(name,age,salary,"Accountant");
			this.password = password;
	}
	
    /**
     * 数字转换
     * 随着公司业务的开展，国际性业务也有相应的拓宽，
     * 会计们需要一个自动将数字转换为英文显示的功能。
     * 编辑们希望有一种简约的方法能将数字直接转化为数字的英文显示。
     * 
     * 给定一个非负整数型输入，将数字转化成对应的英文显示，省略介词and
     * 正常输入为非负整数，且输入小于2^31-1;
     * 如果有非法输入（字母，负数，范围溢出等），返回illegal
     * 
     * 示例：
     * 
     * number: 2132866842
     * return: "Two Billion one Hundred Thirty Two Million Eight Hundred Sixty Six Thousand Eight Hundred Forty Two"
     *
     * number：-1
     * return："illegal"
     * @param number
     */
    public  String numberToWords (String number){
    	int num = -1;
    	String illegalInfo = "illegal";
    	try {
			num = Integer.parseInt(number);
			if (num<0)return illegalInfo;
		}catch (Exception e){
    		return illegalInfo;
		}
		if(num==-1)return illegalInfo;
		return numberToWords(num);
    }
	private String numberToWords(int num) {
		if(num==0)return "Zero";
		String numStr = Integer.toString(num);
		String temp;
		StringBuilder res = new StringBuilder();
		//根据字符串长度分类
		if(numStr.length()>9){
			temp = numStr.substring(0,1);
			res.append(belowtens[temp.charAt(0)-'1']);
			res.append(" Billion ");
			numStr = numStr.substring(1);
		}
		if(numStr.length()>6){
			int f = numStr.length()-6;
			temp = numStr.substring(0,f);
			numStr = numStr.substring(f);
			temp = smallNumToWords(temp);
			if(temp!=null&&temp.length()>0){
				res.append(temp);
				res.append("Million ");
			}
		}
		if(numStr.length()>3){
			int f = numStr.length()-3;
			temp = numStr.substring(0,f);
			numStr = numStr.substring(f);
			temp = smallNumToWords(temp);
			if(temp!=null&&temp.length()>0) {
				res.append(temp);
				res.append("Thousand ");
			}
		}
		res.append(smallNumToWords(numStr));
		return res.toString().trim();//使用trim去除前后空格
	}
	//对小于1000的数字的分析，迭代思想
	private String smallNumToWords(String temp){
		if(temp==null)return null;
		int length = temp.length();
		int x = temp.charAt(0)-'1';
		if(length==3){
			if(x!=-1){
				return belowtens[x]+" Hundred "+smallNumToWords(temp.substring(1));
			}else{
				return smallNumToWords(temp.substring(1));
			}
		}else if(length==2){
			if(x!=-1){
				if(x==0){
					String t = temp.substring(1);
					int x2 = t.charAt(0)-'0';
					return tens[x2]+" ";
				}
				return overtens[x-1]+" "+smallNumToWords(temp.substring(1));
			}else{
				return smallNumToWords(temp.substring(1));
			}
		}else if(length==1){
			if(x!=-1){
				return belowtens[x]+" ";
			}else return "";
		}
		return null;
	}
    /**
     * 检验密码
     * 由于会计身份的特殊性，对会计的密码安全有较高的要求，
     * 会计的密码需要由8-20位字符组成；
     * 至少包含一个小写字母，一个大写字母和一个数字，不允许出现特殊字符；
     * 同一字符不能连续出现三次 (比如 "...ccc..." 是不允许的, 但是 "...cc...c..." 可以)。
     * 
     * 如果密码符合要求，则返回0;
     * 如果密码不符合要求，则返回将该密码修改满足要求所需要的最小操作数n，插入、删除、修改均算一次操作。
     *
     * 示例：
     * 
     * password: HelloWorld6
     * return: 0
     *
     * password: HelloWorld
     * return: 1
     *
     * @param
     */
    public  int checkPassword(){
		int size = password.length();
		int[] rt = new int[size];//用于记录当前位置的字母重复次数（以最后的一个重复字母位置为准）
		int LowerCase = 1,UpperCase = 1,Num = 1;//默认缺少一个大写字母，一个小写字母，一个数字（即空字符串）
		for (int i = 0; i < size;) {
			char c = password.charAt(i);
			if (c >= 'a' && c <= 'z') LowerCase = 0;
			if (c >= 'A' && c <= 'Z') UpperCase = 0;
			if (c >= '1' && c <= '9') Num = 0;

			int j = i;
			while (i < size && c == password.charAt(i)) i++;
			rt[j] = i - j;
		}

		int miss = LowerCase + UpperCase + Num;
		int res = 0;
		if (size < 8){
			res = Math.max(miss,8 - size);
			//aaaaaa1
			if(miss==1&&8-size==1){
				int max = 0;
				for(int m:rt){
					if(max<m)max = m;
				}
				if(max>4)res = 2;
			}
		}else{
			int over = Math.max(size - 20, 0);
			int left = 0;
			res += over;
			for (int k = 1; k < 3; ++k) {
				for (int i = 0; i < size && over > 0; ++i) {
					if (rt[i] < 3 || rt[i] % 3 != (k - 1)) continue;
					rt[i] -= Math.min(over,k);
					over -=k;
				}
			}
			for (int i = 0; i < size; ++i) {
				if (rt[i] >= 3 && over > 0) {
					int need = rt[i] - 2;
					rt[i] -= over;
					over -= need;
				}
				if (rt[i] >= 3) left += rt[i] / 3;
			}
			res += Math.max(miss, left);
		}

		return res;

    }
}

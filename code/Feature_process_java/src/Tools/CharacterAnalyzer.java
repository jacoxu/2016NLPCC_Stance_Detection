package Tools;

/**
 * @author cBrain
 *
 */ 
public class CharacterAnalyzer {
	// java unicode字符集
	// http://doc.java.sun.com/DocWeb/api/java.lang.Character.UnicodeBlock
	// http://hi.baidu.com/qing419925094/item/bb32a7b915273871244b09c9
	public static final boolean isChinese(char c) {
		return (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)?true:false; 
	}
	
	public static final boolean isNumber(char c)
	{
		return ((c<='9')&&(c>='0'))?true:false;
	}

	public static final boolean isEnglish(char c)
	{
		return (((c<='z')&&(c>='a'))||((c<='Z')&&(c>='A')))?true:false;
	}
	
	public static final boolean isWhiteSpace(char c)
	{
		return c == ' ';
	}
	
	public static final boolean isRegularSymbol(char c)
	{
		return (c=='*')||(c=='?');
	}
	
	//判断是否是 中文/英文/数字/常见标点 过滤掉其他字符
	public static final boolean isGoodCharacter(char c)
	{
		if(filterUnicode(c))
			return false;
		if(isChinese(c))
			return true;
		if(isWhiteSpace(c))
			return true;
//		if (isSpecialSymbol(c)) //filter the special symbol
//			return false;
		if(isNumber(c))
			return true;
		if(isEnglish(c))
			return true;
//		if(isSolrKeywords(c)) //filter the solr keywords
//			return false;
//		if(isBiaoDian(c))
//			return true;
//		if(isRegularSymbol(c))
//			return true;
//		if(isOtherBiaoDian(c))
//			return true;
		return false;
	}
	
	private static boolean filterUnicode(char c) {
		//方法1：直接利用UnicodeBlock
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	      || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
	     return true;
	    }
	    //方法2：直接通过Unicode编码集剔除
		//ASCII 标点符号
		if((c>=0x0020)&&(c<=0x002F)||(c>=0x003A)&&(c<=0x0040)||(c>=0x005B)&&(c<=0x0060)||(c>=0x007B)&&(c<=0X007E))
			return true;
		//拉丁文第一增补集标点符号
		if((c>=0x00A0)&&(c<=0x00BF))
			return true;
		//通用标点符号
		if((c>=0x2000)&&(c<=0x206F))
			return true;
		//增补标点符号
		if((c>=0x2E00)&&(c<=0x2E7F))
			return true;
		//CJK标点符号
		if((c>=0x3000)&&(c<=0x303F))
			return true;
		//全角ASCII标点符号
		if((c>=0xFF01)&&(c<=0xFF0F)||(c>=0xFF1A)&&(c<=0xFF20)||(c>=0xFF3B)&&(c<=0xFF40)||(c>=0xFF5B)&&(c<=0xFF5E))
			return true;
		//竖排标点符号
		if((c>=0xFE10)&&(c<=0xFE1F))
			return true;
		
		//方法3：通过词典过滤与前台保证一致性
//		if (SmsBase.getSymbolSet().contains((int)c)) return true;
		
		return false;
	}
	
	private static boolean isSpecialSymbol(char c) {
		//"`~!@#$%^&*()-_=+[]{}|;:',<.>/?\
//		if(c=='"'||c=='`'||c=='~'||c=='!'||c=='@'||c=='#'||c=='$'||
//		   c=='%'||c=='^'||c=='&'||c=='*'||c=='('||c==')'||c=='-'||
//		   c=='_'||c=='='||c=='+'||c=='['||c==']'||c=='{'||c=='}'||
//		   c=='|'||c==';'||c==':'||c=='\''||c==','||c=='<'||c=='.'||
//		   c=='>'||c=='/'||c=='?'||c=='\\')
//			return true;
		
		//ASCII 标点符号
		if((c>=0x0020)&&(c<=0x002F)||(c>=0x003A)&&(c<=0x0040)||(c>=0x005B)&&(c<=0x0060)||(c>=0x007B)&&(c<=0X007E))
			return true;
		//全角ASCII标点符号
		if((c>=0xFF01)&&(c<=0xFF0F)||(c>=0xFF1A)&&(c<=0xFF20)||(c>=0xFF3B)&&(c<=0xFF40)||(c>=0xFF5B)&&(c<=0xFF5E))
			return true;
		return false;
	}

	private static boolean isSolrKeywords(char c) {
		if(c==':'||c=='?'||c=='*'||c=='"'||c=='('||c==')')
			return true;
		return false;
	}

	public static final boolean isOtherBiaoDian(char c)
	{
		return (c=='\"')||(c=='$')||(c==':')||(c=='|')||(c==',');
	}
	
	public static final boolean isBiaoDian(char c)
	{
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	      || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
	     return true;
	    }
	    return false;
	}
	/**
	 * @param c
	 * @return
	 */
	public static final boolean isSymbol(char c)
	{
//	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
//	      || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
//	      || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//	      || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//	      || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//	      || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
//	     return true;
//	    }
//	    return false;
		if(c=='-'||c=='/'||c=='_'||c==':')
			return true;
		return false;
	}
	public static void main (String[] args){
//		String ss = "、!@#$%^&*北京()！@￥%……&*（）明天！＠＃￥％……＆×";
		String ss ="℃＄¤￠￡‰§№☆★○●◎◇◆□■△▲※→←↑↓〓ⅰⅱ我ⅲⅳⅴⅵⅶ北 京ⅷⅸⅹ⒈⒉，你呢？⒊⒋⒌⒍⒎⒏";
		StringBuffer str = new StringBuffer();
		char test1 = 0x25;
		System.out.println(test1);    
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			int test = ch[i];
			System.out.println(ch[i]+" - 0x"+Integer.toHexString(test));
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch[i]);
			if (!isGoodCharacter(ch[i]))
			continue;
			str.append(ch[i]);
		}
		System.out.println(ss);
		System.out.println(str.toString());
	}
	
	private static boolean filterSympoTest(char c) {
		//方法1：直接利用UnicodeBlock
//	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//	    if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//	      || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
//	     return true;
//	    }
	    //方法2：直接通过Unicode编码集剔除
		if (isWhiteSpace(c)) return false;
		//ASCII 标点符号
		if((c>=0x0020)&&(c<=0x002F)||(c>=0x003A)&&(c<=0x0040)||(c>=0x005B)&&(c<=0x0060)||(c>=0x007B)&&(c<=0X007E))
			return true;
		//拉丁文第一增补集标点符号
		if((c>=0x00A0)&&(c<=0x00BF))
			return true;
		//通用标点符号
		if((c>=0x2000)&&(c<=0x206F))
			return true;
		//增补标点符号
		if((c>=0x2E00)&&(c<=0x2E7F))
			return true;
		//CJK标点符号
		if((c>=0x3000)&&(c<=0x303F))
			return true;
		//全角ASCII标点符号
		if((c>=0xFF01)&&(c<=0xFF0F)||(c>=0xFF1A)&&(c<=0xFF20)||(c>=0xFF3B)&&(c<=0xFF40)||(c>=0xFF5B)&&(c<=0xFF5E))
			return true;
		//竖排标点符号
		if((c>=0xFE10)&&(c<=0xFE1F))
			return true;

		return false;
	}
}
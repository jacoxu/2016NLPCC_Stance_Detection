package Tools;

public class StringAnalyzer {

    
	private static String extractSendTime(String ss) {
		String[] tokens = ss.split("{$}");
		return tokens[0];
	}
	private static String removeShortTerm(String ss){
		StringBuffer sb = new StringBuffer();
		String[] tokens = ss.split(" ");
		for(int i = 0;i<tokens.length;i++)
		{
			if(tokens[i].length()>1)
			{
				sb.append(tokens[i]);
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	public static String extractChineseCharacterWithoutSpace(String ss) {
		Boolean lastCharTag = true;
		StringBuffer str = new StringBuffer();
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if(CharacterAnalyzer.isChinese(ch[i]))
			{
				str.append(ch[i]);
			}
		}
		return str.toString();
	}
	
	public static String extractGoodCharacter(String ss){  
		if(ss == null)
			return null;
		Boolean lastCharTag = true;
		StringBuffer str = new StringBuffer();
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if(CharacterAnalyzer.isGoodCharacter(ch[i])){
				str.append(ch[i]);
			}else {
				str.append(' ');
			}
		}
		return str.toString().replaceAll("\\s+", " ").trim();
	}
	
	public static String extractChineseCharacter(String ss) {
		Boolean lastCharTag = true;
		StringBuffer str = new StringBuffer();
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if(CharacterAnalyzer.isChinese(ch[i]))
			{
				if(lastCharTag)
				{
					str.append(ch[i]);
				}
				else
				{
					str.append(" ");
					str.append(ch[i]);
					lastCharTag = true;
				}
			}
			else
			{
				lastCharTag = false;
			}
		}
		//return removeShortTerm(str.toString());
		if(str.toString().length() == 0)
		{
			return "";
		}
		
		return str.toString().toLowerCase();
	}
	
	public static String extractEnglishCharacter(String ss){
		Boolean lastCharTag = true;
		StringBuffer str = new StringBuffer();
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if(CharacterAnalyzer.isEnglish(ch[i])){
				if(lastCharTag){
					str.append(ch[i]);
				}
				else{
					str.append(" ");
					str.append(ch[i]);
					lastCharTag = true;
				}
			}
			else{
				lastCharTag = false;
			}
		}
		if(str.toString().length() == 0){
			return null;
		}
		
		return str.toString().toLowerCase();
	}
	
	public static Boolean isNumberString(String ss){
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if(!CharacterAnalyzer.isNumber(ch[i]))
				return false;
		}
//		int telephoneNumberLength = 14;
//		if(ss.length()> telephoneNumberLength || ss.length() < 10)//TODO
//			return false;
//		
		return true;
	}

	public static Boolean isNumberString2(String ss){
		char[] ch = ss.toCharArray();
		for (int i = 1; i < ch.length-1; i++) {
			if(!CharacterAnalyzer.isNumber(ch[i]))
				return false;
		}
		int telephoneNumberLength = 14;
		if(ss.length()> telephoneNumberLength || ss.length() < 10)
			return false;
		
		return true;
	}
	
	
	
	public static String extractNumberCharacter(String ss){
		Boolean lastCharTag = true;
		StringBuffer str = new StringBuffer();
		char[] ch = ss.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			//if(characterAnalyzer.isNumber(ch[i])||characterAnalyzer.isSymbol(ch[i]))
			if(CharacterAnalyzer.isNumber(ch[i])){
				if(lastCharTag){
					str.append(ch[i]);
				}
				else{
					str.append(" ");
					str.append(ch[i]);
					lastCharTag = true;
				}
			}
			else{
				lastCharTag = false;
			}
		}
		if(str.toString().length() == 0){
			return null;
		}
		return str.toString();
	}
	
	public static void main(String args[]){
		String testString = "丨~~@昨天喝12了[酒]，今天|丨血压高。 大事没办了1 6，小-事耽误了。 横批是：他阿了吊嬳!!!";
		System.out.println(extractGoodCharacter(testString));
		System.out.println(extractEnglishCharacter(testString));
		System.out.println(extractNumberCharacter(testString));
	}
}

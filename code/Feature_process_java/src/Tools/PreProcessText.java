package Tools;
import java.io.IOException;

import Tools.StringAnalyzer;
import TypeTrans.Full2Half;

public class PreProcessText {
	static public String preProcess4Task1(String inputStr, String tmpRelationP, String tmpEntityS, String tmpEntityO) throws IOException{
		if (inputStr.length()<1) return inputStr;
		//强制限制了实体词分开
		if (tmpRelationP!=null) {
			if (tmpRelationP.length()==4) { //传闻不和、同为校花、昔日情敌、绯闻女友 等关系 重心词在后面
				if (!inputStr.contains(tmpRelationP)) {
					tmpRelationP = tmpRelationP.substring(2);
				}
			} 
			inputStr = inputStr.replaceAll(tmpRelationP, " "+tmpRelationP+" ");
		}
		inputStr = inputStr.replaceAll(tmpEntityS, " "+tmpEntityS+" ");
		inputStr = inputStr.replaceAll(tmpEntityO, " "+tmpEntityO+" ");
		inputStr=Full2Half.ToDBC(inputStr);//全角转半角						
		inputStr=inputStr.toLowerCase();//字母全部小写
		inputStr=inputStr.replaceAll("\\s+", " ");//多个空格缩成单个空格
		inputStr = StringAnalyzer.extractGoodCharacter(inputStr); //去除所有特殊字符
		//                           无词性                                                                       带词性
		inputStr = WordSegment_Ansj.splitWord(inputStr)+"\t"+WordSegment_Ansj.splitWordwithTag(inputStr);//进行分词
		
		return inputStr;
	} 
	static public String preProcess4Task2(String inputStr) throws IOException{
		if (inputStr.length()<1) return inputStr;
		inputStr=Full2Half.ToDBC(inputStr);//全角转半角						
		inputStr=inputStr.toLowerCase();//字母全部小写
		inputStr=inputStr.replaceAll("\\s+", " ");//多个空格缩成单个空格
		inputStr = StringAnalyzer.extractGoodCharacter(inputStr); //去除所有特殊字符
		//                           无词性                                                                       带词性
		inputStr = WordSegment_Ansj.splitWordwithOutTag4Task2(inputStr);//进行分词
		
		return inputStr.trim();
	}
	
	static public String preProcess4NLPCC2016(String inputStr, String topic) throws IOException{
		if (inputStr.length()<1) return inputStr;
		inputStr=inputStr.replaceAll("#"+topic+"#", " ");//（1）过滤掉HashTag的标识
		inputStr=inputStr.replaceAll("http://t.cn/(.{7})", " ");//（2）过滤掉http://t.cn/[7个字符]
		//（3）过滤掉一些特殊的分享标识，如：
		//（分享[10个字符以内]）(分享[10个字符以内]) 【分享[10个字符以内]】
		//（来自[10个字符以内]）(来自[10个字符以内]) 【来自[10个字符以内]】
		inputStr=inputStr.replaceAll("（分享(.{0,9})）", " ");
		inputStr=inputStr.replaceAll("\\(分享(.{0,9})\\)", " ");
		inputStr=inputStr.replaceAll("【分享(.{0,9})】", " ");
		
		inputStr=inputStr.replaceAll("（来自(.{0,9})）", " ");
		inputStr=inputStr.replaceAll("\\(来自(.{0,9})\\)", " ");
		inputStr=inputStr.replaceAll("【来自(.{0,9})】", " ");
		//（4）过滤掉所有的@标识，如@腾讯新闻客户端 @[10个字符以内]后接另一个@、空格或换行符
		String[] inputStr_sub = inputStr.split("\\s+");
		StringBuffer inputStr_bf = new StringBuffer();
		for (String tmpinputStr_sub:inputStr_sub) {
			tmpinputStr_sub = tmpinputStr_sub+"<eos>";
			tmpinputStr_sub = tmpinputStr_sub.replaceAll("@(.{0,9})@", "@");	
			tmpinputStr_sub = tmpinputStr_sub.replaceAll("@(.{0,9}) ", " ");
			tmpinputStr_sub = tmpinputStr_sub.replaceAll("@(.{0,9})<eos>", " ");
			tmpinputStr_sub = tmpinputStr_sub.replaceAll("<eos>", "");
			inputStr_bf.append(tmpinputStr_sub);
			inputStr_bf.append(" ");
		}
		inputStr = inputStr_bf.toString().trim();
		inputStr_bf = null;	
		
		inputStr=Full2Half.ToDBC(inputStr);//（5）全角转半角					
		inputStr=inputStr.toLowerCase();//（6）字母全部小写
		inputStr=inputStr.replaceAll("\\s+", " ");//（7）多个空格缩成单个空格
		inputStr = StringAnalyzer.extractGoodCharacter(inputStr); //（8）去除所有特殊字符
		inputStr = WordSegment_Ansj.splitWord(inputStr);//（9）进行分词
		
		return inputStr.trim();
	} 	

	private static boolean isITSuffixSpamInfo(String tmpQuerySnippet, String tmpEntityS, String tmpEntityO) {
		if ((tmpQuerySnippet.contains(tmpEntityS)||tmpQuerySnippet.contains(tmpEntityO))
				&&tmpQuerySnippet.length()>4) {
			return false;
		}else {
			return true;
		}
	}
}

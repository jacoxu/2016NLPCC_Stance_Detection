package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Step04_01_TaskA_ChiSquare {
	public static void main(String[] args) throws Exception {

		//读取训练数据文件
		String dataPathStr="./../../data/RefineData/Step01_topics/";
		String resultsPathStr="./../../data/RefineData/Step04_chisquare/";
		File resultsPathFile = new File(resultsPathStr);
		if (!resultsPathFile.exists()) resultsPathFile.mkdir();
		
	    String train_labeled_text_pre = dataPathStr+"TaskA_train_labeled_text";
	    String train_labeled_tag_pre = dataPathStr+"TaskA_train_labeled_tag";

	    //主题到文件后缀的映射
	    ArrayList<String> topic2filesuffix = new ArrayList<String>();
	    topic2filesuffix.add("iphonese");
	    topic2filesuffix.add("bianpao");
	    topic2filesuffix.add("fankong");
	    topic2filesuffix.add("ertai");
	    topic2filesuffix.add("jinmo");
	    int low_feq = 1;
	    boolean hasTestData = false;
	    
	    for (int topic_idx = 0; topic_idx < topic2filesuffix.size(); topic_idx++) {
			String usrWordDict = resultsPathStr + "usrWordDict_low_feq_" + String.valueOf(low_feq) 
					+ "_"+ topic2filesuffix.get(topic_idx);
			BufferedWriter wordDictFileW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(usrWordDict)), "UTF-8"));			

			String taskA_ChiSquare = resultsPathStr + "TaskA_chiSquare_" 
					+ topic2filesuffix.get(topic_idx);
			BufferedWriter taskA_ChiSquareFileW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(taskA_ChiSquare)), "UTF-8"));	
			
	    	String train_labeled_text = 
					train_labeled_text_pre+"_"+topic2filesuffix.get(topic_idx);
			FileReader train_labeled_text_fr = new FileReader(train_labeled_text);
			BufferedReader train_labeled_text_br = new BufferedReader(train_labeled_text_fr);

			String train_labeled_tag = 
					train_labeled_tag_pre+"_"+topic2filesuffix.get(topic_idx);
			FileReader train_labeled_tag_fr = new FileReader(train_labeled_tag);
			BufferedReader train_labeled_tag_br = new BufferedReader(train_labeled_tag_fr);

			//先读入处理好的训练语料
			int wordReadNum = 0;
			int wordWriteNum = 0;

			HashMap<String, Long> wordMap = new HashMap<String, Long>();
			System.out.println("Start to read wordSet ...");
			//train_labeled_text
			int lineNum = 0;
			String tempLine;
			while ((tempLine = train_labeled_text_br.readLine()) != null) {
				lineNum++;
				String[] wordArraysStr = tempLine.trim().split("\\s+");
				for (int i = 0; i < wordArraysStr.length; i++) {
					String tmpWord = wordArraysStr[i].trim();
					if (tmpWord.length()<1) continue;
					if (wordMap.containsKey(tmpWord)) {
						wordMap.put(tmpWord, wordMap.get(tmpWord)+1);
					}else {
						wordMap.put(tmpWord,(long) 1);
						wordReadNum++;
					}
				}
				if (lineNum%1000 ==0) {
					System.out.println("hasProcessed train data numbers:" + lineNum);
				}
			}
			System.out.println("train_labeled_text total lineNum:"+lineNum
					+", and wordSet.size():"+wordMap.size());
			train_labeled_text_br.close();

			//开始筛选low_feq>3的并输出
			System.out.println("raw wordMap size:" + wordMap.size());			
			Iterator iter = wordMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String, Long> entry = (Map.Entry) iter.next();
				String tmpWord = entry.getKey();
				long tmp_low_feq = entry.getValue();
				if (tmp_low_feq < low_feq) {
					wordMap.remove(tmpWord);
					continue;
				}
				wordWriteNum++;
				wordDictFileW.write(tmpWord+"\t"+wordWriteNum+"\n");
			}
			System.out.println("wordReadNum:" + wordReadNum);
			System.out.println("wordWriteNum:" + wordWriteNum);
			System.out.println("refined wordMap size:" + wordMap.size());
			wordDictFileW.close();			
			
			//开始输出卡方格式
			//tag \t text
			lineNum = 0;
			train_labeled_text_fr = new FileReader(train_labeled_text);
			train_labeled_text_br = new BufferedReader(train_labeled_text_fr);			
			String tempTag;
			String tempText;
			String[] tokensStr;
			StringBuffer tmpLineBuffer = new StringBuffer();
			while ((tempTag = train_labeled_tag_br.readLine()) != null) {
				lineNum++;
				tempText = train_labeled_text_br.readLine();
				
				tokensStr = tempText.trim().split("\\s+");

				if (!(tokensStr.length<1)) {
					for (int j = 0; j < tokensStr.length; j++) {
						String tempToken = tokensStr[j];
						if (wordMap.containsKey(tempToken.trim())) {
							tmpLineBuffer.append(tempToken + " ");
						}else {
							System.out.println("error: the map has not contain the word:"
									+ tempToken+" in Line:" + lineNum);
						}
					}
					taskA_ChiSquareFileW.write(tempTag+"\t"+tmpLineBuffer.toString().trim()+"\n");	
				}				
				
				tmpLineBuffer.delete(0, tmpLineBuffer.length());
				if (lineNum%1000 ==0) {
					System.out.println("hasProcessed train_unlabeled_text data numbers:" + lineNum);
				}
			}
			taskA_ChiSquareFileW.close();
			System.out.println("taskA_ChiSquareFileW total lineNum:"+lineNum
					+", and wordSet.size():"+wordMap.size());
			train_labeled_tag_br.close();
			train_labeled_text_br.close();

		}
	}
}
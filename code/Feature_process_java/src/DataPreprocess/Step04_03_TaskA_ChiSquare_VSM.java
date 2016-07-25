package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Step04_03_TaskA_ChiSquare_VSM {
	public static void main(String[] args) throws Exception {
		//Test
//		String tempLine ="宸濄伄娴併倢銇倛銇?28521"; 
//		String[] tokensStr  = tempLine.split("\t");
		//利用纯文本和wordmap构建基于词频的向量空间模型，用于STH预处理
		// all = [test_data;train_data]!
		String dataPathStr = "./../../data/RefineData/Step01_topics/";
		String resultsPathStr = "./../../data/RefineData/Step04_chisquare/";
		
	    String train_labeled_text_pre = dataPathStr+"TaskA_train_labeled_text";
	    String train_unlabeled_text_pre = dataPathStr+"TaskA_train_unlabeled_text";
	    String test_unlabeled_text_pre = dataPathStr+"TaskA_test_unlabeled_text";
	    
	    //主题到文件后缀的映射
	    ArrayList<String> topic2filesuffix = new ArrayList<String>();
	    topic2filesuffix.add("iphonese");
	    topic2filesuffix.add("bianpao");
	    topic2filesuffix.add("fankong");
	    topic2filesuffix.add("ertai");
	    topic2filesuffix.add("jinmo");
	    
	    boolean hasTestData = true;
	    
	    for (int topic_idx = 0; topic_idx < topic2filesuffix.size(); topic_idx++) {
			String wordMapStr = resultsPathStr + "usrWordDict_chi2_refined_" + topic2filesuffix.get(topic_idx);
			
			String vsmBW_train_labeledStr = resultsPathStr + "/vsm_train_labeled_chi2_"
											+ topic2filesuffix.get(topic_idx);
			String vsmBW_train_unlabeledStr = resultsPathStr+"/vsm_train_unlabeled_chi2_"
											  + topic2filesuffix.get(topic_idx);
			String vsmBW_test_unlabeledStr = resultsPathStr+"/vsm_test_unlabeled_chi2_"
					  						 + topic2filesuffix.get(topic_idx);
			BufferedReader train_labeled_File = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(train_labeled_text_pre
							+"_"+topic2filesuffix.get(topic_idx))), "UTF-8"));
			BufferedReader train_unlabeled_File = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(train_unlabeled_text_pre
							+"_"+topic2filesuffix.get(topic_idx))), "UTF-8"));
			
			//构造训练VSM词频向量空间模型
			BufferedReader wordMapRD = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(wordMapStr)), "UTF-8"));
			creatVSMText(train_labeled_File, wordMapRD, vsmBW_train_labeledStr);
			wordMapRD.close();
			train_labeled_File.close();
			
			wordMapRD = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(wordMapStr)), "UTF-8"));
			creatVSMText(train_unlabeled_File, wordMapRD, vsmBW_train_unlabeledStr);
			wordMapRD.close();
			train_unlabeled_File.close();			
			
			if(hasTestData){
				BufferedReader test_unlabeled_File = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(test_unlabeled_text_pre
								+"_"+topic2filesuffix.get(topic_idx))), "UTF-8"));
				
				wordMapRD = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File(wordMapStr)), "UTF-8"));
				creatVSMText(test_unlabeled_File, wordMapRD, vsmBW_test_unlabeledStr);
				wordMapRD.close();
				test_unlabeled_File.close();
			}
	    }	    

		System.out.println("It is done, ok!");
	}
	
	public static void creatVSMText(BufferedReader sourceTextRD,
			BufferedReader wordMapRD, String vsmBW_Str) throws IOException, Exception {
		System.out.println("Start to create VSM ...!");
		String tempLine;
		//先读入词典
		int wordIdxNum = 1;
		HashMap<String, Integer> wordMap = new HashMap<String,Integer>();

		while ((tempLine = wordMapRD.readLine()) != null) {
			//词典中放着词和索引号，索引号
			if (wordMap.containsKey(tempLine.trim())) {
				System.out.println("Test, the word is replicate:"+tempLine.trim()
						+", in wordIdxNum:"+wordIdxNum);
			}
			if (tempLine.trim().length()==0) continue;
			//wordMap.put(tempLine.trim(), wordIdxNum);
			wordMap.put(tempLine.split("\\s+")[0].trim(), Integer.valueOf(tempLine.split("\\s+")[1]));	
			wordIdxNum =wordIdxNum+1;
		}
		//定义了这个数据集的特征维数
		int dimVector = wordIdxNum-1;
		System.out.println("Has read the dictionary, the size is:"+wordMap.size());
		ArrayList<Integer> wordFreqList = new ArrayList<Integer>();
		int lineNum = 0;
		boolean hasWordFeature = false;
		int hasNoWordFeature = 0;
		StringBuffer tmpVSMBuffer = new StringBuffer();
		while ((tempLine = sourceTextRD.readLine()) != null) {
			hasWordFeature = false;
			//读入一行，即一个文档；
			wordFreqList.clear();
			for (int i = 0; i < dimVector; i++) {
				wordFreqList.add(0);
			}

			String[] tokensStr;
			boolean isvalid = true;
			
			tokensStr = tempLine.trim().split("\\s+");

			if (!(tokensStr.length<1)) {
				for (int j = 0; j < tokensStr.length; j++) {
					String tempToken = tokensStr[j];
					if (wordMap.containsKey(tempToken.trim())) {
						hasWordFeature = true;
						int index = wordMap.get(tempToken.trim());
						if (index>dimVector) {
							System.out.print("Error, and the word is: "+tempToken.trim());
						}
						wordFreqList.set(index-1, wordFreqList.get(index-1)+1);
					}
//					else {
//						System.out.println("error: the map has not contain the word:"
//								+tempToken+" in Line:"+lineNum);
//					}
				}
			}else {
				isvalid = false;
			}
			
			if (!isvalid) {
				System.out.println("warning: the string has lacked contents:"
						+tempLine.trim()+" in Line:"+lineNum);
			}
			for (int tempFreq:wordFreqList) {
				tmpVSMBuffer.append(String.valueOf(tempFreq)+" ");
			}
			//把处理好的文本写入到新的文本文件中
			Result2Txt(vsmBW_Str,tmpVSMBuffer.toString().trim());
			tmpVSMBuffer.delete(0, tmpVSMBuffer.length());
			
			if (!hasWordFeature) {
				hasNoWordFeature++;
			}
			lineNum++;
			if (lineNum%1000 ==0) {
				System.out.println("hasProcessed text numbers:" + lineNum);
			}
		}
		System.out.println("Total number is:"+lineNum+" and no word lines is:"+ hasNoWordFeature);
	}
	public static void Result2Txt(String file, String txt) {
		  try {
			   BufferedWriter os = new BufferedWriter(new OutputStreamWriter(   
		                new FileOutputStream(new File(file),true), "UTF-8")); 
			   os.write(txt + "\n");
			   os.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	 }
}

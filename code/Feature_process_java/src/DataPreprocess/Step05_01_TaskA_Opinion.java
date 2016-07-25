package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Step05_01_TaskA_Opinion {
	public static void main(String[] args) throws Exception {
		//Test
//		String tempLine ="宸濄伄娴併倢銇倛銇?28521"; 
//		String[] tokensStr  = tempLine.split("\t");
		//利用纯文本和wordmap构建基于词频的向量空间模型，用于STH预处理
		// all = [test_data;train_data]!
		String dataPathStr = "./../../data/RefineData/Step01_topics/";
		String corpusPathStr = "./../../corpus/";
		String resultsPathStr = "./../../data/RefineData/Step05_opinion/";
		
	    String train_labeled_text_pre = dataPathStr+"TaskA_train_labeled_text";
	    String test_unlabeled_text_pre = dataPathStr+"TaskA_test_unlabeled_text";
	    
	    //主题到文件后缀的映射
	    ArrayList<String> topic2filesuffix = new ArrayList<String>();
	    topic2filesuffix.add("iphonese");
	    topic2filesuffix.add("bianpao");
	    topic2filesuffix.add("fankong");
	    topic2filesuffix.add("ertai");
	    topic2filesuffix.add("jinmo");
	    
	    boolean hasTestData = true;
	    HashSet<String> opinion_pos_set = new HashSet<String>();
	    HashSet<String> opinion_neg_set = new HashSet<String>();
	    String opinion_pos_str = corpusPathStr + "Hownet/pos_opinion.txt";
	    String opinion_neg_str = corpusPathStr + "Hownet/neg_opinion.txt";
	    loadWordSet(opinion_pos_str, opinion_pos_set);
	    loadWordSet(opinion_neg_str, opinion_neg_set);

	    HashSet<String> sentiment_pos_set = new HashSet<String>();
	    HashSet<String> sentiment_neg_set = new HashSet<String>();
	    String sentiment_pos_str = corpusPathStr + "Tsinghua/tsinghua.positive.gb.txt";
	    String sentiment_neg_str = corpusPathStr + "Tsinghua/tsinghua.negative.gb.txt";
	    loadWordSet(sentiment_pos_str, sentiment_pos_set);
	    loadWordSet(sentiment_neg_str, sentiment_neg_set);
	    
	    for (int topic_idx = 0; topic_idx < topic2filesuffix.size(); topic_idx++) {
			
			String opinion_train_labeledStr = resultsPathStr + "/opinion_train_labeled_"
											+ topic2filesuffix.get(topic_idx);
			String opinion_test_unlabeledStr = resultsPathStr+"/opinion_test_unlabeled_"
					  						 + topic2filesuffix.get(topic_idx);
			BufferedReader train_labeled_File = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(train_labeled_text_pre
							+"_"+topic2filesuffix.get(topic_idx))), "UTF-8"));
			
			//构造训练VSM词频向量空间模型
			creatOpinoinFea(train_labeled_File, opinion_train_labeledStr,
					opinion_pos_set, opinion_neg_set, sentiment_pos_set, sentiment_neg_set);
			train_labeled_File.close();
					
			
			if(hasTestData){
				BufferedReader test_unlabeled_File = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(test_unlabeled_text_pre
								+"_"+topic2filesuffix.get(topic_idx))), "UTF-8"));
				
				creatOpinoinFea(test_unlabeled_File, opinion_test_unlabeledStr,
						opinion_pos_set, opinion_neg_set, sentiment_pos_set, sentiment_neg_set);
				test_unlabeled_File.close();
			}
	    }	    

		System.out.println("It is done, ok!");
	}
	
	private static void loadWordSet(String wordFileStr, HashSet<String> wordSet) throws IOException {
		File wordFile = new File(wordFileStr);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(wordFile);
		} catch (FileNotFoundException fnfe) {
			System.out.println("not found wordFileStr:"+wordFileStr);
		}
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	wordSet.add(tempString.trim());
            }
            System.out.println("load word dictionary... done");
            reader.close();
        }  finally {
            fis.close();
        }
	}
	
	public static void creatOpinoinFea(BufferedReader file_br, String opinion_str
			, HashSet<String> opinion_pos_set, HashSet<String> opinion_neg_set
			, HashSet<String> sentiment_pos_set, HashSet<String> sentiment_neg_set
			) throws IOException, Exception {
		System.out.println("Start to create opinion feature ...!");
		String tempLine;
		//定义了这个数据集的特征维数
		double opinion_pos_num = 0.0;
		double opinion_neg_num = 0.0;
		double sentiment_pos_num = 0.0;
		double sentiment_neg_num = 0.0;
		
		double opinion_pos_score = 0.0;
		double opinion_neg_score = 0.0;
		double sentiment_pos_score = 0.0;
		double sentiment_neg_score = 0.0;
		int lineNum = 0;
		while ((tempLine = file_br.readLine()) != null) {
			opinion_pos_num = 0;
			opinion_neg_num = 0;
			sentiment_pos_num = 0;
			sentiment_neg_num = 0;
			String[] tokensStr;			
			tokensStr = tempLine.trim().split("\\s+");

			for (int j = 0; j < tokensStr.length; j++) {
				String tempToken = tokensStr[j];
				if (opinion_pos_set.contains(tempToken.trim())) {
					opinion_pos_num+=1.0;
				}else if (opinion_neg_set.contains(tempToken.trim())) {
					opinion_neg_num+=1.0;
				}else if (sentiment_pos_set.contains(tempToken.trim())) {
					sentiment_pos_num+=1.0;
				}else if (sentiment_neg_set.contains(tempToken.trim())) {
					sentiment_neg_num+=1.0;
				}
			}
			opinion_neg_num = Math.pow(opinion_neg_num, 1.3);
			sentiment_neg_num = Math.pow(sentiment_neg_num, 1.3);
			opinion_pos_score = 
					(opinion_pos_num+1.0)/(opinion_pos_num+opinion_neg_num+2.0);
			opinion_neg_score = 
					(opinion_neg_num+1.0)/(opinion_pos_num+opinion_neg_num+2.0);
			sentiment_pos_score = 
					(sentiment_pos_num+1.0)/(sentiment_pos_num+sentiment_neg_num+2.0);
			sentiment_neg_score = 
					(sentiment_neg_num+1.0)/(sentiment_pos_num+sentiment_neg_num+2.0);
			
			//把处理好的文本写入到新的文本文件中
			Result2Txt(opinion_str,String.valueOf(opinion_pos_score)+" "+String.valueOf(opinion_neg_score)+" "
					+ String.valueOf(sentiment_pos_score)+" "+String.valueOf(sentiment_neg_score));
			lineNum++;
			if (lineNum%1000 ==0) {
				System.out.println("hasProcessed text numbers:" + lineNum);
			}
		}
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

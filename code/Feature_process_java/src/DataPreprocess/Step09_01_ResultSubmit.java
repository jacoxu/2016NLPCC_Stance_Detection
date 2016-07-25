package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Step09_01_ResultSubmit {
	/**
	 * @param args
	 * @author jacoxu.com-2016/06/23
	 */
	//predictResult_pre, testFile_str, submitFile_str, topic2filesuffix
	private static void dataAnalysis(String predictResult_pre, String testFile_str
			, String submitFile_str, ArrayList<String> topic2filesuffix) {
		
		try {
			String encoding = "UTF-8";
			File testFile = new File(testFile_str);
			BufferedReader testFileReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(testFile), encoding));
			String tmpPredictLineStr;
			int readPredictLineNum = 0;
			String tmpTestLineStr;
			int readTestLineNum = 0;
			//先把测试文件的头部写到提交文件中
			tmpTestLineStr = testFileReader.readLine();
			if (tmpTestLineStr.split("\t").length != 4) {
				System.err.println("Error, in the first line of text file:" + tmpTestLineStr);	
				System.exit(0);
			}
			Result2Txt(submitFile_str, tmpTestLineStr);
			//开始写结果，按照Topic依次进行写入
			String tmpPredictLabel = "UNKNOWN";
			String tmpNewTestStr;
			for (int topic_idx = 0; topic_idx < topic2filesuffix.size(); topic_idx++) {
				System.out.println("Current topic is:"+topic2filesuffix.get(topic_idx));
				String predictResult_str = predictResult_pre + topic2filesuffix.get(topic_idx);
				File predictResultFile = new File(predictResult_str);
				BufferedReader predictResultReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(predictResultFile), encoding));
				while ((tmpPredictLineStr = predictResultReader.readLine()) != null) {
					readPredictLineNum++;
					float predictScore = Float.valueOf(tmpPredictLineStr);
					if (predictScore < -0.2)
						System.err.println("Error, wrong predict score:"
											+ String.valueOf(predictScore));
					else if (predictScore < 0.2)
						tmpPredictLabel = "NONE";
					else if (predictScore < 1.2)
						tmpPredictLabel = "FAVOR";
					else if (predictScore < 2.2)
						tmpPredictLabel = "AGAINST";
					else {
						System.err.println("Error, wrong predict score:"
											+ String.valueOf(predictScore));
					}
					tmpTestLineStr = testFileReader.readLine();
					String[] tmpTestTokens = tmpTestLineStr.split("\t");
					if (tmpTestTokens.length != 4) {
//						System.err.println("Error, in the first line of text file:" + tmpTestLineStr);	
//						System.exit(0);
						System.out.println(tmpTestTokens[0]+"\t"+tmpTestTokens[4]);
					}
					readTestLineNum++;
					tmpNewTestStr = tmpTestTokens[0]+"\t"+tmpTestTokens[1]+"\t"+tmpTestTokens[2]+"\t"+tmpPredictLabel;
					Result2Txt(submitFile_str, tmpNewTestStr);					
				}
				predictResultReader.close();
			}			
			testFileReader.close();
			if (readPredictLineNum != readTestLineNum) {
				System.err.println("Error, readPredictLineNum:" + readPredictLineNum 
						+ ", readTestLineNum"+ readTestLineNum);	
				System.exit(0);
			}
			
		} catch (Exception e) {
			System.out.println("something error when reading the content of the file");
			e.printStackTrace();
		}
		return;
		
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
	
	public static void main(String[] args) {
		//***********测试区域************
		System.out.println("test");

		//***********测试区域************
		
		String dataPathStr="./../../data/";
		String resultsPathStr="./../../data/RefineData/Results/";
		File resultsPathFile = new File(resultsPathStr);
		if (!resultsPathFile.exists()) resultsPathFile.mkdir();

		//进行测试集结果展示
	    String predictResult_pre=resultsPathStr+"predict_";//resultsPathStr+topic;
	    //TODO 这里为测试集的文件名
	    String testFile_str = dataPathStr+"NLPCC2016_Stance_Detection_Task_A_Testdata.txt";
	    String submitFile_str = dataPathStr +"TaskA_prediction_submission.txt";
	    ArrayList<String> topic2filesuffix = new ArrayList<String>();
	    topic2filesuffix.add("bianpao");
	    topic2filesuffix.add("iphonese");
	    topic2filesuffix.add("fankong");
	    topic2filesuffix.add("ertai");
	    topic2filesuffix.add("jinmo");
	    
		long readstart=System.currentTimeMillis();
		dataAnalysis(predictResult_pre, testFile_str, submitFile_str, topic2filesuffix);
        long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to process submission.");
	}

}

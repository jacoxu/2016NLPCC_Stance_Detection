# README #

This is our solution for [NLPCC2016 shared task: Detecting Stance in Chinese Weibo (Task A)](http://tcci.ccf.org.cn/conference/2016/pages/page05_CFPTasks.html) 

@inproceedings{xu2016ensemble,    
  title={Ensemble of Feature Sets and Classification Methods for Stance Detection},    
  author={Xu, Jiaming and Zheng, Suncong and Shi, Jing and Yao, Yiqun and Xu, Bo},    
  booktitle={Natural Language Processing and Chinese Computing (NLPCC)},       
  year={2016},    
  publisher={Springer}    
}    

- This is a supervised task towards five targets. For each target, 600 labled Weibo texts, 600 unlabeled Weibo texts and 3,000 test Weibo texts are provided. The task is to detect the author's stance.      

We give an ensemble framework by integrating various feature sets, such as Paragraph Vector (Para2vec) [1], Latent Dirichlet Allocation (LDA) [2], Latent Semantic Analysis (LSA) [3], Laplacian Eigenmaps (LE) [4] and Locality Preserving Indexing (LPI) [5], and various classification methods, such as Random Forest (RF) [6], Linear Support Vector Machines (SVM-Linear) [7], SVM with RBF Kernel (SVM-RBF) [8] and AdaBoot [9].    

The official results show that our solution of the team "CBrain" achieves one 1st place and one 2nd place on these targets, and the overall ranking is 4th out of 16 teams with 0.6856 F1 score.       

**Team member**: [Jiaming Xu](http://jacoxu.com/?page_id=2), Suncong Zheng, Jing Shi, Yiqun Yao, Bo Xu.    

Please feel free to send me emails (*jacoxu@msn.com*) if you have any problems.  

[1]. Le, Q.V., Mikolov, T.: Distributed representations of sentences and documents. In:ICML. vol. 14, pp. 1188-1196 (2014)    
[2]. Blei, D.M., Ng, A.Y., Jordan, M.I.: Latent dirichlet allocation. Journal of Machine Learning Research 3(Jan), 993-1022 (2003)    
[3]. Deerwester, S., Dumais, S.T., Furnas, G.W., Landauer, T.K., Harshman, R.: Indexing by latent semantic analysis. JASIS 41(6), 391 (1990)    
[4]. Belkin, M., Niyogi, P.: Laplacian eigenmaps for dimensionality reduction and data representation. Neural Computation 15(6), 1373-1396 (2003)    
[5]. He, X., Cai, D., Liu, H., Ma, W.Y.: Locality preserving indexing for document representation. In: SIGIR. pp. 96-103. ACM (2004)    
[6]. Breiman, L.: Random forests. Machine Learning 45(1), 5-32 (2001)    
[7]. Joachims, T.: Learning to classify text using support vector machines: Methods, theory and algorithms. Kluwer Academic Publishers (2002)    
[8]. Scholkopf, B., Smola, A.J.: Learning with kernels: support vector machines, regularization, optimization, and beyond. MIT press (2002)    
[9]. Freund, Y., Schapire, R.E., et al.: Experiments with a new boosting algorithm. In:ICML. vol. 96, pp. 148-156 (1996)    

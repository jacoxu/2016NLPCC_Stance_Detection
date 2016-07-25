function [single_ACC, leave_out_ACC, all_ACC, top_ACC] = TaskA_Feature_ranking(topic, features, parameters, hasTestData)
% 有5个主题词，11个特征组。
% topics = {'iphonese', 'bianpao', 'fankong', 'ertai', 'jinmo'};
% features = {'Para2vec', 'LDA', 'LSA', 'LE', 'LPI', 'VSM_chi2_tf', 'VSM_chi2_tfidf', 'LSA_chi2', 'LE_chi2', 'LPI_chi2', 'opinion_fea'};
parameters.topic = topic;
if hasTestData
    isSelfEvalute = 0; % 0:直接对测试数据进行标注
else
    isSelfEvalute = 1; % 1:从标注数据中抽出 parameters.cross_valid 份进行交叉验证
end
selectFeatureSet = 1; %是否选择特征，1 进行选择直到找到最大值(此时isSelfEvalute必须为1)，0 不进行选择
leave_out_depth = parameters.leave_out_depth;
method = parameters.method;
if strcmp(method, 'SVM_Kernel')
   isKernel = 1; % 1:SVM是否使用Gaussian高斯核，0: SVM采用线性Linear 
else
   isKernel = 0; % 1:SVM是否使用Gaussian高斯核，0: SVM采用线性Linear 
end
hasLoadedData = 1; %0: 还没有加载过数据，1: 已经加载过数据了

if ~hasLoadedData
    % 先加载特征
    for feature = features
        if strcmp(feature, 'Para2vec')
            dataStr = ['./../../code/Para2vec/',topic,'_para2vecs.txt'];
            load(dataStr);
            evalStr = ['fea.Para2vec.train = ', topic, '_para2vecs(1:',num2str(parameters.labeled_num),',2:end);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.Para2vec.test = ', topic, '_para2vecs(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,2:end);'];
                eval(evalStr)
            end
            evalStr = ['clear ', topic, '_para2vecs;'];
            eval(evalStr)
        elseif strcmp(feature, 'LDA')            
            dataStr = ['./../../code/LDA/',topic,'/model-final.theta'];
            load(dataStr);
            fea.LDA.train = model_final(1:parameters.labeled_num,:);
            if hasTestData
                fea.LDA.test = model_final(parameters.labeled_num+parameters.unlabeled_num+1:end,:);
            end
            clear model_final;         
        elseif strcmp(feature, 'LSA')
            dataStr = ['./../../data/RefineData/Step03_lsa_lpi_le/fea_lsa_',topic];
            load(dataStr);
            evalStr = ['fea.LSA.train = fea_lsa_', topic, '(1:',num2str(parameters.labeled_num),',:);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.LSA.test = fea_lsa_', topic, '(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,:);'];
                eval(evalStr)
            end
            evalStr = ['clear fea_lsa_', topic, ';'];
            eval(evalStr)            
        elseif strcmp(feature, 'LE')
            dataStr = ['./../../data/RefineData/Step03_lsa_lpi_le/fea_le_',topic];
            load(dataStr);
            evalStr = ['fea.LE.train = fea_le_', topic, '(1:',num2str(parameters.labeled_num),',:);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.LE.test = fea_le_', topic, '(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,:);'];
                eval(evalStr)
            end
            evalStr = ['clear fea_le_', topic, ';'];
            eval(evalStr)                        
        elseif strcmp(feature, 'LPI')
            dataStr = ['./../../data/RefineData/Step03_lsa_lpi_le/fea_lpi_',topic];
            load(dataStr);
            evalStr = ['fea.LPI.train = fea_lpi_', topic, '(1:',num2str(parameters.labeled_num),',:);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.LPI.test = fea_lpi_', topic, '(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,:);'];
                eval(evalStr)
            end
            evalStr = ['clear fea_lpi_', topic, ';'];
            eval(evalStr)                        
        elseif strcmp(feature, 'VSM_chi2_tf')
            dataStr = ['./../../data/RefineData/Step04_chisquare/vsm_train_labeled_chi2_',topic];
            load(dataStr);
            evalStr = ['fea.VSM_chi2_tf.train = vsm_train_labeled_chi2_', topic,';'];
            eval(evalStr)
            if hasTestData
                dataStr = ['./../../data/RefineData/Step04_chisquare/vsm_test_unlabeled_chi2_',topic];
                load(dataStr);
                evalStr = ['fea.VSM_chi2_tf.test = vsm_test_unlabeled_chi2_', topic,';'];
                eval(evalStr)
            end
            clear vsm_t*;
        elseif strcmp(feature, 'VSM_chi2_tfidf')
            % 这里先不计算tfidf，在后面特征处理时再计算tfidf
            dataStr = ['./../../data/RefineData/Step04_chisquare/vsm_train_labeled_chi2_',topic];
            load(dataStr);
            evalStr = ['fea.VSM_chi2_tfidf.train = vsm_train_labeled_chi2_', topic,';'];
            eval(evalStr)
            if hasTestData
                dataStr = ['./../../data/RefineData/Step04_chisquare/vsm_test_unlabeled_chi2_',topic];
                load(dataStr);
                evalStr = ['fea.VSM_chi2_tfidf.test = vsm_test_unlabeled_chi2_', topic,';'];
                eval(evalStr)
            end
            clear vsm_t*;
        elseif strcmp(feature, 'LSA_chi2')
            dataStr = ['./../../data/RefineData/Step04_chisquare/fea_chi2_lsa_',topic];
            load(dataStr);
            evalStr = ['fea.LSA_chi2.train = fea_chi2_lsa_', topic, '(1:',num2str(parameters.labeled_num),',:);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.LSA_chi2.test = fea_chi2_lsa_', topic, '(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,:);'];
                eval(evalStr)
            end
            evalStr = ['clear fea_chi2_lsa_', topic, ';'];
            eval(evalStr)            
        elseif strcmp(feature, 'LE_chi2')
            dataStr = ['./../../data/RefineData/Step04_chisquare/fea_chi2_le_',topic];
            load(dataStr);
            evalStr = ['fea.LE_chi2.train = fea_chi2_le_', topic, '(1:',num2str(parameters.labeled_num),',:);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.LE_chi2.test = fea_chi2_le_', topic, '(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,:);'];
                eval(evalStr)
            end
            evalStr = ['clear fea_chi2_le_', topic, ';'];
            eval(evalStr)             
        elseif strcmp(feature, 'LPI_chi2')
            dataStr = ['./../../data/RefineData/Step04_chisquare/fea_chi2_lpi_',topic];
            load(dataStr);
            evalStr = ['fea.LPI_chi2.train = fea_chi2_lpi_', topic, '(1:',num2str(parameters.labeled_num),',:);'];
            eval(evalStr)
            if hasTestData
                evalStr = ['fea.LPI_chi2.test = fea_chi2_lpi_', topic, '(',num2str(parameters.labeled_num+parameters.unlabeled_num+1),':end,:);'];
                eval(evalStr)
            end
            evalStr = ['clear fea_chi2_lpi_', topic, ';'];
            eval(evalStr)
        elseif strcmp(feature, 'opinion_fea')
            dataStr = ['./../../data/RefineData/Step05_opinion/opinion_train_labeled_',topic];
            load(dataStr);
            evalStr = ['fea.opinion_fea.train = opinion_train_labeled_', topic,';'];
            eval(evalStr)
            if hasTestData
                dataStr = ['./../../data/RefineData/Step05_opinion/opinion_test_unlabeled_',topic];
                load(dataStr);
                evalStr = ['fea.opinion_fea.test = opinion_test_unlabeled_', topic,';'];
                eval(evalStr)
            end
            clear opinion_t*;
        end
    end
    
    % 加载标签
    load(['./../../data/RefineData/Step01_topics/TaskA_train_labeled_tag_', topic]);
    evalStr = ['train_labels = TaskA_train_labeled_tag_', topic,';'];
    eval(evalStr)    
    clear TaskA_train_labeled_tag_*;
    
    %% 如果没有测试数据的话就把训练数据拆成parameters.cross_valid份进行交叉验证
    if isSelfEvalute
        % features = {'Para2vec', 'LDA', 'LSA', 'LE', 'LPI', 'VSM_chi2_tf', 'VSM_chi2_tfidf', 'LSA_chi2', 'LE_chi2', 'LPI_chi2', 'opinion_fea'};        
        for cv_i = 1:parameters.cross_valid
            fea.Para2vec.train_cv{cv_i} = fea.Para2vec.train(cv_i:parameters.cross_valid:end,:);
            fea.LDA.train_cv{cv_i} = fea.LDA.train(cv_i:parameters.cross_valid:end,:);
            fea.LSA.train_cv{cv_i} = fea.LSA.train(cv_i:parameters.cross_valid:end,:);
            fea.LE.train_cv{cv_i} = fea.LE.train(cv_i:parameters.cross_valid:end,:);
            fea.LPI.train_cv{cv_i} = fea.LPI.train(cv_i:parameters.cross_valid:end,:);
            fea.VSM_chi2_tf.train_cv{cv_i} = fea.VSM_chi2_tf.train(cv_i:parameters.cross_valid:end,:);
            fea.VSM_chi2_tfidf.train_cv{cv_i} = fea.VSM_chi2_tfidf.train(cv_i:parameters.cross_valid:end,:);
            fea.LSA_chi2.train_cv{cv_i} = fea.LSA_chi2.train(cv_i:parameters.cross_valid:end,:);
            fea.LE_chi2.train_cv{cv_i} = fea.LE_chi2.train(cv_i:parameters.cross_valid:end,:);
            fea.LPI_chi2.train_cv{cv_i} = fea.LPI_chi2.train(cv_i:parameters.cross_valid:end,:);
            fea.opinion_fea.train_cv{cv_i} = fea.opinion_fea.train(cv_i:parameters.cross_valid:end,:);
            train_labels_cv{cv_i} = train_labels(cv_i:parameters.cross_valid:end,:);
        end
    end
    
    save(['./../../data/RefineData/Step06_fea_ranking/features_',topic ,'.mat'], 'fea', 'train_labels*')
else
    load(['./../../data/RefineData/Step06_fea_ranking/features_',topic ,'.mat'])
end

parameters.method = method;
parameters.isKernel = isKernel;
parameters.isSelfEvalute = isSelfEvalute;
single_ACC = [];
leave_out_ACC = [];
all_ACC = [];
top_ACC = [];
%% 开始交叉验证
if isSelfEvalute
    % 如果是自己测试的话，选取验证集合中的一部分做为验证集做为开发集
    for cv_i = 1:parameters.cross_valid
        disp (['Current cross valid number is:', num2str(cv_i)]);
        % 开始组装特征
        train_features{1} = [];
        train_features{2} = [];
        train_features{3} = [];
        train_features{4} = [];
        train_features{5} = [];
        train_features{6} = [];
        train_features{7} = [];
        train_features{8} = [];
        train_features{9} = [];
        train_features{10} = [];
        train_features{11} = [];        
        train_labels=[];
        dev_labels=[];
        for cv_ii = 1:parameters.cross_valid
            if cv_ii == cv_i
                dev_features{1} = fea.Para2vec.train_cv{cv_ii};
                dev_features{2} = fea.LDA.train_cv{cv_ii};
                dev_features{3} = fea.LSA.train_cv{cv_ii};
                dev_features{4} = fea.LE.train_cv{cv_ii};
                dev_features{5} = fea.LPI.train_cv{cv_ii};
                dev_features{6} = fea.VSM_chi2_tf.train_cv{cv_ii};
                dev_features{7} = fea.VSM_chi2_tfidf.train_cv{cv_ii};
                dev_features{8} = fea.LSA_chi2.train_cv{cv_ii};
                dev_features{9} = fea.LE_chi2.train_cv{cv_ii};
                dev_features{10} = fea.LPI_chi2.train_cv{cv_ii};
                dev_features{11} = fea.opinion_fea.train_cv{cv_ii};                  
                dev_labels = train_labels_cv{cv_ii};
            else
                train_features{1} = [train_features{1};fea.Para2vec.train_cv{cv_ii}];
                train_features{2} = [train_features{2};fea.LDA.train_cv{cv_ii}];
                train_features{3} = [train_features{3};fea.LSA.train_cv{cv_ii}];
                train_features{4} = [train_features{4};fea.LE.train_cv{cv_ii}];
                train_features{5} = [train_features{5};fea.LPI.train_cv{cv_ii}];
                train_features{6} = [train_features{6};fea.VSM_chi2_tf.train_cv{cv_ii}];
                train_features{7} = [train_features{7};fea.VSM_chi2_tfidf.train_cv{cv_ii}];
                train_features{8} = [train_features{8};fea.LSA_chi2.train_cv{cv_ii}];
                train_features{9} = [train_features{9};fea.LE_chi2.train_cv{cv_ii}];
                train_features{10} = [train_features{10};fea.LPI_chi2.train_cv{cv_ii}];
                train_features{11} = [train_features{11};fea.opinion_fea.train_cv{cv_ii}];                  
                train_labels = [train_labels; train_labels_cv{cv_ii}];
            end
        end
        % 分配好训练集和开发集后计算一下tf-idf
        [train_features{7}, dev_features{7}] = tf_idf(train_features{7}, dev_features{7});        
       %% 开始进行特征选择，如果需要的话，不需要的话则直接模型训练
        feature_groups = length(features);
        disp(['The init feature_groups size is:',num2str(feature_groups)]);
        best_feature_set = ones(1,feature_groups);
        curr_best_feature_index = find(best_feature_set==1);
        parameters.curr_best_feature_index = curr_best_feature_index;
        if selectFeatureSet
            if strcmp(parameters.eval_mode, 'single')            
              %% 特征单独评估一遍
                for fea_i = 1:length(features)
                    % 先进行特征选择，然后进行ACC评估
                    train_fea = train_features{fea_i};
                    dev_fea = dev_features{fea_i};
                    [ACC, predict_scores] = evaluateMode(train_fea, train_labels, dev_fea, dev_labels, parameters);
                    disp(['current feature ',features{fea_i},' take ACC:', num2str(ACC)]);
                    single_ACC(fea_i, cv_i) = ACC;
                end                
            end
            if strcmp(parameters.eval_mode, 'leave_out')      
              %% 特征中依次去除某个特征
                for fea_i = 1:length(features)
                    is_rid_fea = false;
                    for fea_rid_i = 1:leave_out_depth-1
                        if strcmp(features{fea_i}, parameters.leave_out_fea{fea_rid_i})
                            is_rid_fea = true;
                            break;
                        end
                    end
                    if is_rid_fea
                        continue;
                    end
                    train_fea = [];
                    dev_fea = [];                    
                    % 先进行特征选择，然后进行ACC评估
                    for fea_ii = 1:length(features)
                        is_rid_fea = false;
                        for fea_rid_i = 1:leave_out_depth-1
                            if strcmp(features{fea_ii}, parameters.leave_out_fea{fea_rid_i})
                                is_rid_fea = true;
                                break;
                            end
                        end
                        if is_rid_fea
                            continue;
                        end                        
                        if fea_ii ~= fea_i
                            train_fea = [train_fea, train_features{fea_ii}];
                            dev_fea = [dev_fea, dev_features{fea_ii}];
                        end
                    end
                    [ACC, predict_scores] = evaluateMode(train_fea, train_labels, dev_fea, dev_labels, parameters);
                    disp(['current leave out feature ',features{fea_i},' take ACC:', num2str(ACC)]);
                    leave_out_ACC(fea_i, cv_i) = ACC;                
                end                
            end
            if strcmp(parameters.eval_mode, 'all_fea')      
              %% 提取所有特征
                train_fea = [];
                dev_fea = [];                    
                % 先进行特征选择，然后进行ACC评估
                for fea_i = 1:length(features)
                    train_fea = [train_fea, train_features{fea_i}];
                    dev_fea = [dev_fea, dev_features{fea_i}];
                end
                train_fea = full(train_fea);
                dev_fea = full(dev_fea);
                [ACC, predict_scores] = evaluateMode(train_fea, train_labels, dev_fea, dev_labels, parameters);
                disp(['current all feature take ACC:', num2str(ACC)]);
                all_ACC(cv_i) = ACC;                            
            end    
            if strcmp(parameters.eval_mode, 'top_fea')      
              %% 提取所有特征
                train_fea = [];
                dev_fea = [];                    
                % 先进行特征选择，然后进行ACC评估
                for fea_i = 1:parameters.top_fea_k
                    for fea_ii = 1:length(features)
                        if(strcmp(parameters.rank_fea{fea_i}, features{fea_ii}))
                            break;
                        end
                    end
                    train_fea = [train_fea, train_features{fea_ii}];
                    dev_fea = [dev_fea, dev_features{fea_ii}];
                end
                train_fea = full(train_fea);
                dev_fea = full(dev_fea);
                [ACC, predict_scores] = evaluateMode(train_fea, train_labels, dev_fea, dev_labels, parameters);
                disp(['current top feature take ACC:', num2str(ACC)]);
                top_ACC(cv_i) = ACC;                            
            end               
        else
           %% 不进行特征选择直接计算
            [ACC, predict_scores] = evaluateMode(train_fea, train_labels, dev_fea, dev_labels, parameters);
        end
    end
    disp('cross_valid endding ...')
else
    train_features{1} = fea.Para2vec.train;
    train_features{2} = fea.LDA.train;
    train_features{3} = fea.LSA.train;
    train_features{4} = fea.LE.train;
    train_features{5} = fea.LPI.train;
    train_features{6} = fea.VSM_chi2_tf.train;
    train_features{7} = fea.VSM_chi2_tfidf.train;
    train_features{8} = fea.LSA_chi2.train;
    train_features{9} = fea.LE_chi2.train;
    train_features{10} = fea.LPI_chi2.train;
    train_features{11} = fea.opinion_fea.train;
    train_labels = train_labels;
    
    test_features{1} = fea.Para2vec.test;
    test_features{2} = fea.LDA.test;
    test_features{3} = fea.LSA.test;
    test_features{4} = fea.LE.test;
    test_features{5} = fea.LPI.test;
    test_features{6} = fea.VSM_chi2_tf.test;
    test_features{7} = fea.VSM_chi2_tfidf.test;
    test_features{8} = fea.LSA_chi2.test;
    test_features{9} = fea.LE_chi2.test;
    test_features{10} = fea.LPI_chi2.test;
    test_features{11} = fea.opinion_fea.test;    
    test_labels = zeros(length(fea.Para2vec.test(:,1)),1);
    % 分配好训练集和测试集后计算一下tf-idf
    train_features{7}, test_features{7} = tf_idf(train_features{7}, test_features{7});
end
end



function [ACC, predict_scores]=evaluateMode(labeled_fea, train_labels, unlabeled_fea, test_labels, parameters)
method = parameters.method;
isKernel = parameters.isKernel;
isSelfEvalute = parameters.isSelfEvalute;

if (strncmp(method,'SVM', 3))
%% 进行SVM模型训练和预测
    labeled_fea = sparse(labeled_fea);
    unlabeled_fea = sparse(unlabeled_fea);
    tic
    y = zeros(length(unlabeled_fea(:,1)),1);
    if ~isKernel
        disp('start train linear SVM model ...')
        model = train(train_labels, labeled_fea, '-q');
        disp('start predict test data via linear SVM ...')
        [predict_label, accuracy, predict_scores] = predict(y,unlabeled_fea,model);
    else
        disp('start train kernel SVM model ...')
        model = svmtrain(train_labels,labeled_fea,['-q -c 1 -g ',int2str(length(labeled_fea(1,: )))]);
        disp('start predict test data via kernel SVM model ...')
        [predict_label, accuracy, predict_scores] = svmpredict(y,unlabeled_fea,model);
    end
    toc
    ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
    if ~isKernel
        disp(['[SVM-Linear for ',parameters.topic,'] Accuracy is ',num2str(ACC)])
    else
        disp(['[SVM-RBF for ',parameters.topic,'] Accuracy is ',num2str(ACC)])
    end

elseif (strcmp(method,'RF'))
    %% 进行随机森林分类器（Random Forest）训练和预测
    labeled_fea = full(labeled_fea);
    unlabeled_fea = full(unlabeled_fea);
    nTree = 500;
    tic
    disp('start train Random Forest model ...')
    model = TreeBagger(nTree, labeled_fea, train_labels);
    disp('start predict test data via Random Forest model ...')
    [predict_label, predict_scores] = predict(model, unlabeled_fea);
    predict_label = str2num(cell2mat(predict_label));
    toc
    
    ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
    disp(['[Random Forest for ',parameters.topic,'] Accuracy is ',num2str(ACC)])

elseif (strcmp(method,'MLR'))
    %% 进行逻辑回归(多项式MultiNomial logistic Regression)训练和预测
    labeled_fea = full(labeled_fea);
    unlabeled_fea = full(unlabeled_fea);
    tic
    disp('start train model ...')
    train_labels = train_labels+1;
    model = mnrfit(labeled_fea, train_labels);
    disp('start predict test data ...')
    predict_scores = mnrval(model, unlabeled_fea);
    train_labels = train_labels -1;
    [~, predict_label] = max(predict_scores, [], 2);
    predict_label = predict_label - 1;
    toc
    ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
    disp(['[MLR for ',parameters.topic,'] Accuracy is ',num2str(ACC)])
elseif (strcmp(method, 'Ensemble'))
    tic    
    disp('start train Ensemble model ...')    
    model = fitensemble(labeled_fea, train_labels, 'AdaBoostM2', 500, 'tree', 'type', 'classification');
    disp('start predict test data ...')
    [predict_label, predict_scores] = predict(model, unlabeled_fea);
    toc
    ACC = length(find(predict_label == test_labels))/length(test_labels)*100;    
    disp(['[Ensemble for ',parameters.topic,'] Accuracy is ',num2str(ACC)])
end
end


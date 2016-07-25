%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NLPCC2016 利用随机森林方法进行特征选择和排序
% 微博用户立场检测 -- 2016/06/16
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clc;clear;
warning off;
addpath(genpath([pwd '/']));
%% 配置项
topics = {'iphonese', 'bianpao', 'fankong', 'ertai', 'jinmo'};
% topics = {'jinmo'};
features = {'Para2vec', 'LDA', 'LSA', 'LE', 'LPI', 'VSM_chi2_tf', 'VSM_chi2_tfidf', 'LSA_chi2', 'LE_chi2', 'LPI_chi2', 'opinion_fea'};
hasTestData = false;
parameters.cross_valid = 4;
parameters.method = 'Ensemble'; % RF(Random Forest), SVM, SVM_Kernel, Ensemble
parameters.eval_mode = 'leave_out'; % single, leave_out, all_fea, top_fea
parameters.top_fea_k = 5;
parameters.leave_out_depth = 4;
%%
for topic_i = 1:length(topics)
    rand('state',0)
    randn('state',0)    
    topic = topics{topic_i};
    disp (['Current topic is:', topic]);
    if strcmp(topic, 'iphonese')
        if strcmp(parameters.eval_mode,'top_fea')
            if strcmp(parameters.method,'RF')
                parameters.rank_fea = {'LE_chi2', 'LPI_chi2', 'LSA_chi2', 'VSM_chi2_tfidf', 'VSM_chi2_tf'};
            elseif strcmp(parameters.method,'SVM')
                parameters.rank_fea = {'VSM_chi2_tf', 'LE_chi2', 'LPI_chi2', 'LSA_chi2', 'VSM_chi2_tfidf'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.rank_fea = {'LE_chi2', 'LSA_chi2', 'LPI_chi2', 'LSA', 'LE'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.rank_fea = {'LE_chi2', 'LPI_chi2', 'LSA_chi2', 'LSA', 'Para2vec'};
            end
        end
        if strcmp(parameters.eval_mode,'leave_out')
            if strcmp(parameters.method,'RF')
                parameters.leave_out_fea = {'LPI', 'LSA', 'Para2vec', 'VSM_chi2_tf'};
            elseif strcmp(parameters.method,'SVM')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'Para2vec', 'LE'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.leave_out_fea = {'Para2vec', 'LDA', 'VSM_chi2_tf'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'VSM_chi2_tf', 'LSA_chi2'};
            end
        end        
        parameters.labeled_num = 600;
        parameters.unlabeled_num = 600;
        parameters.test_num = 0;
    elseif strcmp(topic, 'bianpao')  
        if strcmp(parameters.eval_mode,'top_fea')
            if strcmp(parameters.method,'RF')
                parameters.rank_fea = {'LE_chi2', 'LPI_chi2', 'LSA_chi2', 'VSM_chi2_tfidf', 'VSM_chi2_tf'};
            elseif strcmp(parameters.method,'SVM')
                parameters.rank_fea = {'LPI_chi2', 'LSA_chi2', 'VSM_chi2_tfidf', 'VSM_chi2_tf', 'LE_chi2'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.rank_fea = {'LPI_chi2', 'LE_chi2', 'LSA_chi2', 'LDA', 'LE'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.rank_fea = {'LSA_chi2', 'LE_chi2', 'LPI_chi2', 'LSA', 'Para2vec'};
            end
        end
        if strcmp(parameters.eval_mode,'leave_out')
            if strcmp(parameters.method,'RF')
                parameters.leave_out_fea = {'VSM_chi2_tf', 'opinion_fea', 'VSM_chi2_tfidf'};
            elseif strcmp(parameters.method,'SVM')
                parameters.leave_out_fea = {'VSM_chi2_tf', 'LDA', 'LE'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.leave_out_fea = {'VSM_chi2_tf', 'VSM_chi2_tfidf', 'Para2vec'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'LE_chi2', 'LSA'};
            end
        end         
        parameters.labeled_num = 600;
        parameters.unlabeled_num = 600;
        parameters.test_num = 0;
    elseif strcmp(topic, 'fankong')
        if strcmp(parameters.eval_mode,'top_fea')
            if strcmp(parameters.method,'RF')
                parameters.rank_fea = {'LSA_chi2', 'VSM_chi2_tfidf', 'VSM_chi2_tf', 'LPI_chi2', 'LPI'};
            elseif strcmp(parameters.method,'SVM')
                parameters.rank_fea = {'VSM_chi2_tf', 'LSA_chi2', 'VSM_chi2_tfidf', 'LSA', 'LDA'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.rank_fea = {'LSA_chi2', 'LSA', 'LDA', 'LPI', 'LE'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.rank_fea = {'LSA_chi2', 'LPI', 'LPI_chi2', 'VSM_chi2_tf', 'LSA'};
            end
        end
        if strcmp(parameters.eval_mode,'leave_out')
            if strcmp(parameters.method,'RF')
                parameters.leave_out_fea = {'opinion_fea', 'VSM_chi2_tfidf', 'LPI'};
            elseif strcmp(parameters.method,'SVM')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'LPI_chi2', 'LE'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.leave_out_fea = {'Para2vec', 'LDA', 'LSA'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'LPI_chi2', 'LSA'};
            end
        end         
        parameters.labeled_num = 600;
        parameters.unlabeled_num = 600;
        parameters.test_num = 0;
    elseif strcmp(topic, 'ertai')
        if strcmp(parameters.eval_mode,'top_fea')
            if strcmp(parameters.method,'RF')
                parameters.rank_fea = {'LE_chi2', 'LSA_chi2', 'LPI_chi2', 'VSM_chi2_tf', 'LSA'};
            elseif strcmp(parameters.method,'SVM')
                parameters.rank_fea = {'LPI_chi2', 'LE_chi2', 'VSM_chi2_tfidf', 'VSM_chi2_tf', 'LSA_chi2'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.rank_fea = {'LSA_chi2', 'LPI_chi2', 'LE_chi2', 'LSA', 'LE'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.rank_fea = {'LSA_chi2', 'LE_chi2', 'LPI_chi2', 'LSA', 'LE'};
            end
        end
        if strcmp(parameters.eval_mode,'leave_out')
            if strcmp(parameters.method,'RF')
                parameters.leave_out_fea = {'LPI', 'VSM_chi2_tfidf', 'Para2vec'};
            elseif strcmp(parameters.method,'SVM')
                parameters.leave_out_fea = {'opinion_fea', 'LSA', 'LE'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.leave_out_fea = {'Para2vec', 'LDA', 'VSM_chi2_tf'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'Para2vec', 'LSA'};
            end
        end         
        parameters.labeled_num = 600;
        parameters.unlabeled_num = 599;
        parameters.test_num = 0;
    elseif strcmp(topic, 'jinmo')
        if strcmp(parameters.eval_mode,'top_fea')
            if strcmp(parameters.method,'RF')
                parameters.rank_fea = {'LSA_chi2', 'LE_chi2', 'LPI_chi2', 'LSA', 'LPI'};
            elseif strcmp(parameters.method,'SVM')
                parameters.rank_fea = {'LSA_chi2', 'VSM_chi2_tf', 'VSM_chi2_tfidf', 'LPI_chi2', 'LE_chi2'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.rank_fea = {'LSA_chi2', 'LPI_chi2', 'LE_chi2', 'LDA', 'LE'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.rank_fea = {'LSA_chi2', 'LPI_chi2', 'LE_chi2', 'LSA', 'LE'};
            end
        end
        if strcmp(parameters.eval_mode,'leave_out')
            if strcmp(parameters.method,'RF')
                parameters.leave_out_fea = {'LDA', 'LE', 'Para2vec'};
            elseif strcmp(parameters.method,'SVM')
                parameters.leave_out_fea = {'VSM_chi2_tf', 'LPI', 'LE_chi2'};
            elseif strcmp(parameters.method,'SVM_Kernel')
                parameters.leave_out_fea = {'Para2vec', 'LDA', 'VSM_chi2_tf'};            
            elseif strcmp(parameters.method,'Ensemble')
                parameters.leave_out_fea = {'VSM_chi2_tfidf', 'LPI_chi2', 'Para2vec'};
            end
        end         
        parameters.labeled_num = 586;
        parameters.unlabeled_num = 600;
        parameters.test_num = 0;
    else
        disp(['Error, wrong topic: ', tpoic]);
    end
    [single_ACC{topic_i}, leave_out_ACC{topic_i}, all_ACC{topic_i}, top_ACC{topic_i}] = TaskA_Feature_ranking(topic, features, parameters, hasTestData);
end


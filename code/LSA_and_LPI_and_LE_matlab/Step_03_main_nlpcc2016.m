%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NLPCC2016 评测用向量生成 AE, LE, LPI
% 微博用户立场检测 -- 2016/06/16
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clc;clear;
addpath(genpath([pwd '/']));
%% Please select one clustering method here.
method='LSA'; % LSA, Spectral_LE, LPI
topics = {'iphonese', 'bianpao', 'fankong', 'ertai', 'jinmo'};
parameters.nLowVec=50;
hasTestData = true;
%%
for i = 1:length(topics)
    topic = topics{i};
    Step_03_nlpcc2016(method, topic, parameters, hasTestData);
end


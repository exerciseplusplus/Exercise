# -*- coding: utf-8 -*-
__author__='keven'
import math
import random

def loadData(filename):
    """load data in filename"""
    data = []
    for line in open(filename):
        user,item,record,timestamp = line.split()
        data.append((user,item,int(record)))
    return data

def splitData(data,M,K,seed):
    """split data into train and test randomly"""
    test={}
    train={}
    random.seed(seed)
    for user,item,record in data:
        if random.randint(0,M)==K:
            test.setdefault(user,{})
            test[user][item]=record
        else:
            train.setdefault(user,{})
            train[user][item]=record
    return train,test

def precisionAndRecall(train,test,W,K,N):
    """compute precision and recall for model"""
    hit=0;pre=0;rec=0
    for user in train.keys():
        tu = test.get(user,{})
        rank=recommender(user,train,W,K,N)
        for item,pui in rank.items():
            if item in tu:
                hit+=1
        pre+=N
        rec+=len(tu)
    return hit/(pre*1.0),hit/(rec*1.0)


def itemSimilarity(train,method='IUF'):
    """compute COS user similarity for model"""
    C=dict()
    N=dict()
    for u,items in train.items():
        for i  in items:
            N[i]=N.get(i,0)+1
            for j in items:
                if i==j:continue
                C.setdefault(i,{})
                if method=='IUF':
                    C[i][j]=C[i].get(j,0)+1/math.log(1+len(items)*1.0)
                else:
                    C[i][j]=C[i].get(j,0)+1
    W=dict()
    for i,related_items in C.items():
        for j,cij in related_items.items():
            W.setdefault(i,{})
            W[i][j]=cij/math.sqrt(N[i]*N[j])
    return W

def recommender(user,train,W,K,N):
    """recommend to user N item according to K max similarity item"""
    rank=dict()
    interacted_items=train[user]
    for i,pi in interacted_items.items():
        for j,wj in sorted(W[i].items(),key=lambda c:c[1], reverse=True)[0:K]:
            if j in interacted_items:
                continue
            rank[j]=rank.get(j,0)+pi*wj
    return dict(sorted(rank.items(),key = lambda c :c[1],reverse = True)[0:N])

def testItemCF(filename):
    """test UserCF algorithm"""
    data=loadData(filename)
    train,test=splitData(data,5,1,2)
    W=itemSimilarity(train,method='IUF')
    # rank=recommender('344',train,W,K)
    # print rank
    print 'K\t\tprecision\trecall'
    for k in [5,10,20,40,80,160]:
        pre,rec=precisionAndRecall(train,test,W,k,10)
        print "%3d\t\t%.2f%%\t\t%.2f%%" % (k,rec * 100,pre * 100)

if __name__=='__main__':
    testItemCF('us.data')
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


def userSimilarity(train,method='User-IIF'):
    """compute COS user similarity for model"""
    # get inverse table for item users
    item_users=dict()
    for u,items in train.items():
        for i in items.keys():
            if i not in item_users:
                item_users[i]=set()
            item_users[i].add(u)
    C=dict()
    N=dict()
    for i,users in item_users.items():
        for u  in users:
            N[u]=N.get(u,0)+1
            for v in users:
                if u==v:continue
                C.setdefault(u,{})
                if method=='User-IIF':
                    C[u][v]=C[u].get(v,0)+1/math.log(1+len(users)*1.0)
                else:
                    C[u][v]=C[u].get(v,0)+1
    W=dict()
    for u,related_users in C.items():
        for v,cuv in related_users.items():
            W.setdefault(u,{})
            W[u][v]=cuv/math.sqrt(N[u]*N[v])
    return W

def recommender(user,train,W,K,N):
    """recommend to user N item according to K max similarity user"""
    rank=dict()
    interacted_items=train[user]
    for v,wuv in sorted(W[user].items(),key=lambda c:c[1], reverse=True)[0:K]:
        for i ,rvi in train[v].items():
            if i in interacted_items:
                continue
            # rank[i]=rank.get(i,0)+wuv*rvi
            rank[i]=rank.get(i,0)+wuv
    return dict(sorted(rank.items(),key = lambda c :c[1],reverse = True)[0:N])

def testUserCF(filename):
    """test UserCF algorithm"""
    data=loadData(filename)
    train,test=splitData(data,5,1,2)
    W=userSimilarity(train,method='User-IIF')
    # rank=recommender('344',train,W,K)
    # print rank
    print 'K\t\tprecision\trecall'
    for k in [5,10,20,40,80,160]:
        pre,rec=precisionAndRecall(train,test,W,k,10)
        print "%3d\t\t%.2f%%\t\t%.2f%%" % (k,rec * 100,pre * 100)

if __name__=='__main__':
    testUserCF('u.data')
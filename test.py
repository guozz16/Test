from test2 import day
import pandas as pd 
import matplotlib.pyplot as plt 
import numpy as np
df=pd.read_excel("STLF_DATA_IN_1.xls",index_col=0,header=None,sheet_name="负荷数据")
df2=pd.read_excel("STLF_DATA_IN_1.xls",index_col=0,header=None,sheet_name="气象数据")
days=[]
for i in range(len(df)):
	d=df.index[i]
	try:
		w=dict(zip(df2.loc[d][1].values,df2.loc[d][2].values))
	except KeyError: 
		days.append(day(d,df.loc[d]))
	else:
		days.append(day(d,df.loc[d],w))
print(days[-2].date)
print(days[-2].load)
print(days[-2].weather)
# d=day(df.iloc[0,0],df.iloc[0,1:],df2.iloc[0,])
# # b=set(df2[1])
# # print(b)
# print(d.date)
# print(d.load)
# print(d.weather)
# print(a)
# fig = plt.figure()
# plt.plot(a)
# plt.show()
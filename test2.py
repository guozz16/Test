import numpy as np
class day:
	def __init__(self,date,load,weather={}):
		self,
		self.date=date
		self.load=np.array(load)
		self.weather={}
		self.weather.update(weather)
		self.max_load=self.load.max()
		self.min_load=self.load.min()
		self.mean_load=self.load.mean()

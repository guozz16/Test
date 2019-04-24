
from pypower.api import ppoption, runpf
import numpy as np
import case as cs

PVOUT=50 #MW
UNIT=0.001 #p.u.
UNIT2=3  #MVar
HIGH = 1.09 #p.u.
LOW = 0.91 #p.u.

class PowerSys():
    def __init__(self):
        # 0: Gen up
        # 1: Gen down
        # 2: SVM up
        # 3: SVM down
        self.action_space = [0,1,2,3]
        self.pvout=PVOUT
        self.ppopt = ppoption(VERBOSE=0,OUT_ALL=0,OUT_SYS_SUM=False,OUT_BUS=False,OUT_BRANCH=False) #output format control
        self.observation = {'V0':None, 'P2':None, 'Q1':None}
        self.state={'loss':None, 'Vmin':None, 'Vmax':None}

    def reset(self):
        self.ppc = cs.case3()
        self.ppc['gen'][2][1]=self.pvout
        self.results, self.success = runpf(self.ppc,self.ppopt)
        self.observation['V0']=self.results['gen'][0][5]
        self.observation['P2']=self.results['gen'][2][1]
        self.observation['Q1']=self.results['gen'][1][2]
        self.state=self.getstate()
        return np.array(list(self.observation.values()))

    def step(self, action):

        if action == 0:   # Gen up by UNIT
            if self.ppc['gen'][0][5]<HIGH:
                self.ppc['gen'][0][5] += UNIT
        elif action == 1:   # Gen down by UNIT
            if self.ppc['gen'][0][5]>LOW:
                self.ppc['gen'][0][5] -= UNIT
        elif action == 2:   # SVG up by UNIT2
            if self.ppc['gen'][1][2]<self.ppc['gen'][1][3]:
                self.ppc['gen'][1][2] += UNIT2
        elif action == 3:   # SVG down by UNIT2
            if self.ppc['gen'][1][2]>self.ppc['gen'][1][4]:
                self.ppc['gen'][1][2] -= UNIT2

        #Use pypower to calculate pf
        self.results, self.success = runpf(self.ppc,self.ppopt)

        #get the next state 
        s_=self.getstate()

        #get observation
        self.observation['V0']=self.results['gen'][0][5]
        self.observation['P2']=self.results['gen'][2][1]
        self.observation['Q1']=self.results['gen'][1][2]

        # reward function
        # if voltage is out of range, reward = -1
        if s_['Vmin']<=LOW or s_['Vmax']>=HIGH:
            reward = -1
            done = True
        # if loss is lower than before, reward = 1
        elif s_['loss']<self.state['loss']:
            reward = 1
            done = False
        else:
            reward = 0
            done = False

        self.state = s_

        return np.array(list(self.observation.values())), reward, done

    def getstate(self):
        _state={}
        _state['Vmax'] = max(self.results['bus'][:,7])
        _state['Vmin'] = min(self.results['bus'][:,7])
        _state['loss'] = sum(self.results['gen'][:,1]) - sum(self.results['bus'][:,2])
        return _state
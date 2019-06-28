import numpy as np 
from pypower.api import ppoption, runpf
from case2 import case
import networkx as nx  
import matplotlib.pyplot as plt
import matplotlib.cm as cm
import matplotlib.colors as col
VUNIT=0.001 #p.u
PUNIT=1 #MVar
QUNIT=3  #MVar
HIGH = 1.09 #p.u.
LOW = 0.91 #p.u.
DICT1 = {} # {bus number: P} dict of uncontrollable randomnized generator and its set value

class PowerSys():
    def __init__(self):
        self.action_space = []
        self.ppopt = ppoption(VERBOSE=0,OUT_ALL=0,OUT_SYS_SUM=False,OUT_BUS=False,OUT_BRANCH=False) #output format control
        self.state={'loss':None, 'Vmin':None, 'Vmax':None}
        self.bus_list=[]
        self.genmap = {} #{bus number: gen number}
        self.actmap = {} #{0~n_actions: act number}
        # assign case
        self.ppc = case()
        g = nx.Graph()
        for bus in self.ppc['bus']:
            self.bus_list.append(int(bus[0]))
            g.add_node(int(bus[0]))
        for brch in self.ppc['branch']:
            g.add_edge(int(brch[0]),int(brch[1]))
        self.pos = nx.kamada_kawai_layout(g)
        red = '#ff0000'   #red
        green = '#00ff00'     #green
        blue = '#0000ff'     #blue
        self.cmap = col.LinearSegmentedColormap.from_list('cmap',[blue,green,red])

    def reset(self):
        
        # find Vtheta net
        for b in self.ppc['bus']:
            if(b[1]==3):
                self.vbus = int(b[0])
                break

        # for vbus generator, add 2 actions (up/down)(V)
        if(self.vbus in self.ppc['gen'][:,0]):
            self.action_space.append(-1)
            self.action_space.append(-2)
        # for each generator, add 4 actions (up/down)(P/V)(P/Q)
        for index,g in enumerate(self.ppc['gen']):
            self.genmap[int(g[0])]=index
            i=int(g[0])
            # skip Vtheta net
            if (i==self.vbus):
                continue
            # set uncontrollable generator
            elif i in DICT1.keys():
                g[1] = DICT1[i]
            else:
                self.action_space.append(4*i)
                self.action_space.append(4*i+1)
                self.action_space.append(4*i+2)
                self.action_space.append(4*i+3)
        # build actmap
        for i,act in enumerate(self.action_space):
            self.actmap[i] = act
        # initialize powerflow
        self.results, self.success = runpf(self.ppc,self.ppopt)
        self.observation = self.getobservation()
        self.state = self.getstate()
        return self.observation

    def step(self, action):
        action = self.actmap[action] # transfer act
        if(action>0):
            num = self.genmap[int(action/4)] # gen number
            move = int(action%4) # type of move
        reward = 0 # Default reward and done state
        done = False
        if action == -1:
            self.ppc['gen'][self.genmap[self.vbus]][5] += VUNIT
        elif action == -2:
            self.ppc['gen'][self.genmap[self.vbus]][5] -= VUNIT
        elif move == 0:   # P up by UNIT
            self.ppc['gen'][num][1] += PUNIT
        elif move == 1: # P down by UNIT
            self.ppc['gen'][num][1] -= PUNIT
        elif move == 2 and self.ppc['bus'][int(action/4)][1] == 2:
            self.ppc['gen'][num][5] += VUNIT
        elif move == 2 and self.ppc['bus'][int(action/4)][1] == 1:
            self.ppc['gen'][1][2] += QUNIT
        elif move == 3 and self.ppc['bus'][int(action/4)][1] == 2:
            self.ppc['gen'][num][5] -= VUNIT
        elif move == 3 and self.ppc['bus'][int(action/4)][1] == 1:
            self.ppc['gen'][1][2] -= QUNIT

        # limit PQ of generator
        for g in self.ppc['gen']:
            if g[1]>g[8]>0:
                g[1] = g[8]
            elif g[1]<g[9]:
                g[1] = g[9]
            elif g[2]>g[3]:
                g[2] = g[3]
            elif g[2]<g[4]:
                g[2] = g[4]

        # randomnize the output of uncontrolled generator
        # for g in self.ppc['gen']:
        #     i=int(g[0])
        #     if i in DICT1.keys():
        #         g[1] += 10*np.random.rand()-5
    
        #Use pypower to calculate pf
        self.results, self.success = runpf(self.ppc,self.ppopt)

        #get the next state 
        s_=self.getstate()

        #get observation
        self.observation = self.getobservation()

        # reward function
        # if voltage is out of range, reward = -1
        if s_['Vmin']<=LOW or s_['Vmax']>=HIGH:
            reward = 0
            done = True
        # if loss is lower than before, reward = 1
        elif s_['loss']<self.state['loss']:
            reward = 1
            done = False

        self.state = s_

        return self.observation, reward, done

    def getstate(self):
        _state={}
        _state['Vmax'] = max(self.results['bus'][:,7])
        _state['Vmin'] = min(self.results['bus'][:,7])
        _state['loss'] = sum(self.results['gen'][:,1]) - sum(self.results['bus'][:,2])
        return _state

    def getobservation(self):
        _observation = np.array([self.results['bus'][self.vbus][7]]) # get voltage 
        for g in self.results['gen']:
            _observation = np.append(_observation,g[1]) # get P
            if(self.ppc['bus'][int(g[0])][1]==2):
                _observation = np.append(_observation,g[5]) # get V
            else :
                _observation = np.append(_observation,g[2]) # get Q
        return _observation

    def show(self,k=0):
        g1 = nx.DiGraph() # P flow
        g2 = nx.DiGraph() # Q flow
        P_colors = []
        Q_colors = []
        V_colors = []
        theta_colors = []

        for brch in self.results['branch']:
            P_colors.append(abs(brch[13]))
            if brch[13]>0:
                g1.add_edge(int(brch[0]),int(brch[1]))
            else:
                g1.add_edge(int(brch[1]),int(brch[0]))
        for brch in self.results['branch']:
            Q_colors.append(abs(brch[14]))
            if brch[14]>0:
                g2.add_edge(int(brch[0]),int(brch[1]))
            else:
                g2.add_edge(int(brch[1]),int(brch[0]))
        for bus in self.results['bus']:
            V_colors.append(bus[7])
            theta_colors.append(bus[8])
        edges=nx.draw_networkx_edges(g1, self.pos, width=2, alpha=0.5,edge_color=P_colors,
            edge_cmap=self.cmap,arrows=False)
        plt.clf()
        plt.axis('off')
        plt.title("Vmax:"+str(round(self.state['Vmax'],2))+"   "
            "Vmin:"+str(round(self.state['Vmin'],2))+"   "
            "Loss:"+str(round(self.state['loss'],3)))
        cbar=plt.colorbar(edges)
        cbar.set_label('$P(MW)$')
        nx.draw_networkx_nodes(g1,self.pos,nodelist=self.bus_list,
            cmap=self.cmap,node_color=theta_colors,node_size=300,alpha=0.8)
        nx.draw_networkx_edges(g1, self.pos, width=2, alpha=0.5,edge_color=P_colors,
            edge_cmap=self.cmap,arrows=True)
        nx.draw_networkx_labels(g1, self.pos)
        plt.savefig("Figure_1.png",dpi=150)
        if k==1 or k==3:
            plt.show()
        edges=nx.draw_networkx_edges(g2, self.pos, width=2, alpha=0.5,edge_color=Q_colors,
            edge_cmap=self.cmap,arrows=False)
        plt.clf()
        plt.axis('off')
        plt.title("Vmax:"+str(round(self.state['Vmax'],2))+"   "
            "Vmin:"+str(round(self.state['Vmin'],2))+"   "
            "Loss:"+str(round(self.state['loss'],3)))
        cbar=plt.colorbar(edges)
        cbar.set_label('$Q(Mvar)$')
        nx.draw_networkx_nodes(g2,self.pos,nodelist=self.bus_list,
            cmap=self.cmap,node_color=V_colors,node_size=300,alpha=0.8)
        nx.draw_networkx_edges(g2, self.pos, width=2, alpha=0.5,edge_color=Q_colors,
            edge_cmap=self.cmap,arrows=True)
        nx.draw_networkx_labels(g2, self.pos)
        plt.savefig("Figure_2.png",dpi=150)
        if k==2 or k==3:
            plt.show()
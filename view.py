import numpy as np 
from pypower.api import ppoption, runpf
from pypower.api import case39 as case
import networkx as nx  
import matplotlib.pyplot as plt
import matplotlib.cm as cm
import matplotlib.colors as col

red = '#ff0000'   #red
green = '#00ff00'     #green
blue = '#0000ff'     #blue
cmap1 = col.LinearSegmentedColormap.from_list('cmap',[blue,green,red])
cm.register_cmap(cmap=cmap1)

class FlowView():
    def __init__(self):
        self.ppopt = ppoption(VERBOSE=0,OUT_ALL=0,OUT_SYS_SUM=False,OUT_BUS=False,OUT_BRANCH=False) #output format control
        self.state={'loss':None, 'Vmin':None, 'Vmax':None}
        self.bus_list=[]
        self.ppc = case()
        # initialize network layout
        g = nx.Graph()
        for bus in self.ppc['bus']:
            self.bus_list.append(int(bus[0]))
            g.add_node(int(bus[0]))
        for brch in self.ppc['branch']:
            g.add_edge(int(brch[0]),int(brch[1]))
        self.pos = nx.kamada_kawai_layout(g)
        self.results, self.success = runpf(self.ppc,self.ppopt)
        self.state = self.getstate()
        self.show()

    def getstate(self):
        _state={}
        _state['Vmax'] = max(self.results['bus'][:,7])
        _state['Vmin'] = min(self.results['bus'][:,7])
        _state['loss'] = sum(self.results['gen'][:,1]) - sum(self.results['bus'][:,2])
        return _state

    def show(self, k=0):
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
            edge_cmap=cm.get_cmap('cmap'),arrows=False)
        plt.clf()
        plt.axis('off')
        plt.title("Real Power Flow vs. Bus Voltage Angle")
        cbar=plt.colorbar(edges)
        cbar.set_label('$P(MW)$')
        nx.draw_networkx_nodes(g1,self.pos,nodelist=self.bus_list,
            cmap=cm.get_cmap("cmap"),node_color=theta_colors,node_size=300,alpha=0.8)
        nx.draw_networkx_edges(g1, self.pos, width=2, alpha=0.5,edge_color=P_colors,
            edge_cmap=cm.get_cmap('cmap'),arrows=True)
        nx.draw_networkx_labels(g1, self.pos)
        plt.savefig("Figure_1.png")
        # plt.show()
        edges=nx.draw_networkx_edges(g2, self.pos, width=2, alpha=0.5,edge_color=Q_colors,
            edge_cmap=cm.get_cmap('cmap'),arrows=False)
        plt.clf()
        plt.axis('off')
        plt.title("Imaginary Power Flow vs. Bus Voltage Magnitude")
        cbar=plt.colorbar(edges)
        cbar.set_label('$Q(Mvar)$')
        nx.draw_networkx_nodes(g2,self.pos,nodelist=self.bus_list,
            cmap=cm.get_cmap("cmap"),node_color=V_colors,node_size=300,alpha=0.8)
        nx.draw_networkx_edges(g2, self.pos, width=2, alpha=0.5,edge_color=Q_colors,
            edge_cmap=cm.get_cmap('cmap'),arrows=True)
        nx.draw_networkx_labels(g2, self.pos)
        plt.savefig("Figure_2.png")
        # plt.show()
            
env = FlowView()
print(env.state['Vmax'],env.state['Vmin'],env.state['loss'])

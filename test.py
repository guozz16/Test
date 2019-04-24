import pypower.api as pp
import networkx as nx  
import matplotlib.pyplot as plt

g = nx.Graph()

case = pp.case39()
gen_list = []
bus_list = []
for gen in case['gen']:
	gen_list.append(int(gen[0]))

for bus in case['bus']:
	bus_list.append(int(bus[0]))

for brch in case['branch']:
	g.add_edge(int(brch[0]),int(brch[1]))

pos = nx.kamada_kawai_layout(g)
nx.draw_networkx_nodes(g,pos,nodelist=gen_list,node_color='g',node_size=500,alpha=0.8)
nx.draw_networkx_nodes(g,pos,nodelist=list(set(bus_list).difference(set(gen_list))),
	node_color='r',node_size=300,alpha=0.8)
nx.draw_networkx_edges(g, pos, width=1.0, alpha=0.5)
nx.draw_networkx_labels(g,pos)
plt.axis('off')
plt.show()
plt.savefig("path.png")
import networkx as nx
import csv
import random

n = 7000000  # Number of nodes
m = 3       # Number of edges to attach from a new node to existing nodes

G = nx.barabasi_albert_graph(n, m)

print("nodes:", len(G.nodes()))
print("edges:", len(G.edges()))

for i, j in G.edges():
    G[i][j]['weight'] = random.random()

with open('company_graph_' + str(n // 1000) + "k.csv", 'w', newline='') as csvfile:
    writer = csv.writer(csvfile)
    for i, j, data in G.edges(data=True):
        writer.writerow([i, j, round(data['weight'], 2)])

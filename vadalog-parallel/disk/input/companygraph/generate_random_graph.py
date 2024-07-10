import csv
import random
import networkx as nx

n = 200000  # Number of nodes
m = 3       # Number of edges to attach from a new node to existing nodes

# Initialize the network with a small initial graph
G = nx.complete_graph(m + 1)

# For each new node in the network
for i in range(m + 1, n):
    # Calculate probabilities for attaching edges based on node degrees
    probabilities = [G.degree(j) for j in G.nodes()]
    total = sum(probabilities)
    probabilities = [p / total for p in probabilities]

    # Choose m edges based on preferential attachment
    targets = random.choices(list(G.nodes()), weights=probabilities, k=m)
    
    # Add edges between the new node and chosen targets
    for target in targets:
        G.add_edge(i, target)
        G[i][target]['weight'] = random.random()

print("nodes:", len(G.nodes()))
print("edges:", len(G.edges()))

with open('scale_free_custom_' + str(n // 1000) + "k.csv", 'w', newline='') as csvfile:
    writer = csv.writer(csvfile)
    for i, j, data in G.edges(data=True):
        writer.writerow([i, j, round(data.get('weight', random.random()), 2)])

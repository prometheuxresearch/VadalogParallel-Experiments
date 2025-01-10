from random import randint
import pandas as pd
import networkx as nx
import random


# To generate synth graphs of different topologies

def generate_tree_graph(height: int, max_degree: int, min_degree: int):
    root = 0
    node_index = 1
    edges = list()
    nodes = [root]
    level_nodes = [root]
    for i in range(0, height):
        new_level_nodes = []
        for node in level_nodes:
            degree = randint(min_degree, max_degree)
            for j in range(0, degree):
                new_node = node_index
                node_index = node_index + 1
                edges.append(tuple([node, new_node]))
                new_level_nodes.append(new_node)
                nodes.append(new_node)
        level_nodes = new_level_nodes
    return edges, nodes


def generate_grid_graph(grid_size: int):
    G = nx.grid_2d_graph(grid_size, grid_size)
    return G.edges(), G.nodes()


def generate_random_graph(nodes_number: int, probability: float):
    G = nx.gnp_random_graph(nodes_number, probability)
    return G.edges(), G.nodes()


def generate_random_weighted_graph(nodes_number: int, probability: float, weight_min: float, weight_max: float):
    # Create an empty graph
    G = nx.Graph()

    # Add nodes to the graph
    for node in range(nodes_number):
        node_weight = random.uniform(weight_min, weight_max)
        node_weight = round(node_weight, 2)
        G.add_node(node, weight=node_weight)

    # Generate edges with the specified probability
    for u in range(nodes_number):
        for v in range(u + 1, nodes_number):
            if random.random() <= probability:
                # Generate a random weight within the specified range
                weight = random.uniform(weight_min, weight_max)
                weight = round(weight, 2)
                # Add the weighted edge to the graph
                G.add_edge(u, v, weight=weight)

    return G.edges(data=True), G.nodes(data=True)


def create_random_graph():
    print("Random Graphs")
    # G5K
    edges, nodes = generate_random_graph(5000, 0.002)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("G5K.csv", index=False)

    # G10K
    edges, nodes = generate_random_graph(10000, 0.001)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("G10K.csv", index=False)

    # G20K
    edges, nodes = generate_random_graph(20000, 0.001)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("G20K.csv", index=False)

    # G40K
    edges, nodes = generate_random_graph(40000, 0.001)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("G40K.csv", index=False)


def create_grid_graph():
    print("Grid Graphs")
    # Grid 150
    edges, nodes = generate_grid_graph(150)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("Grid150.csv", index=False)

    # Grid 250
    edges, nodes = generate_grid_graph(250)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("Grid250.csv", index=False)


def create_trees():
    print("Tree Graphs")
    # Tree 11
    edges, nodes = generate_tree_graph(11, 3, 2)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("Tree11.csv", index=False)

    # Tree 15
    edges, nodes = generate_tree_graph(13, 3, 2)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("Tree15.csv", index=False)

    # Tree 17
    edges, nodes = generate_tree_graph(15, 3, 2)
    print(len(edges))
    print(len(nodes))
    columns = ["from", "to"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("Tree17.csv", index=False)


def create_random_weighted_graph():
    print("Random Weighted Graphs")
    edges, nodes = generate_random_weighted_graph(20000, 0.005, 0.51, 0.99)
    nodes = list(map(lambda x: (x[0], x[1]["weight"]), nodes))
    edges = list(map(lambda x: (x[0], x[1], x[2]["weight"]), edges))

    print(len(edges))
    print(len(nodes))

    random.shuffle(edges)

    columns = ["from", "to", "weight"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("loans.csv", index=False)

    random.shuffle(edges)

    columns = ["from", "to", "weight"]
    df = pd.DataFrame(edges, columns=columns)
    df.to_csv("securities.csv", index=False)

    columns = ["node_id", "weight"]
    df = pd.DataFrame(nodes, columns=columns)
    df.to_csv("credit_exposure_100k.csv", index=False)


if __name__ == "__main__":
    create_trees()

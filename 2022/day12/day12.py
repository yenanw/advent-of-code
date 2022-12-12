import sys
import pprint as pp

from queue import PriorityQueue
from dataclasses import dataclass, field


@dataclass
class Vertex:
    key: any
    outgoing: dict = field(default_factory=dict)
    prev: object = None
    visited: bool = False
    distance: int = sys.maxsize

    def __eq__(self, other):
        return self.key == other.key

    def __lt__(self, other):
        return self.key < other.key

    def __hash__(self):
        return hash(self.key)


class Graph:
    def __init__(self):
        self.vert_dict = {}

    def __iter__(self):
        return iter(self.vert_dict.values())

    def add_vertex(self, v):
        if not isinstance(v, Vertex):
            v = self.get_vertex(v)
        self.vert_dict[v.key] = v

    def get_vertex(self, n):
        if n in self.vert_dict:
            return self.vert_dict[n]
        return Vertex(n)

    def has_vertex(self, n):
        return self.get_vertex(n) is not None

    def add_edge(self, frm, to, weight=1):
        if frm.key not in self.vert_dict:
            self.add_vertex(frm)
        if to.key not in self.vert_dict:
            self.add_vertex(to)

        self.vert_dict[frm.key].outgoing[self.vert_dict[to.key]] = weight


def dijkstra(g, start):
    """
    single-source dijkstra's algorithm, modifies the given graph object
    """
    start.distance = 0

    unvisited = PriorityQueue()
    [unvisited.put((v.distance, v)) for v in g]

    while not unvisited.empty():
        uv = unvisited.get()

        curr = uv[1]
        curr.visited = True

        for next in curr.outgoing:
            if next.visited:
                continue

            new_dist = curr.distance + curr.outgoing[next]

            if new_dist < next.distance:
                next.distance = new_dist
                next.prev = curr
                unvisited.put((new_dist, next))


def retrace_path(v, path):
    if v.prev:
        path.append(v.prev.key)
        return retrace_path(v.prev, path)
    return path


def parse():
    with open("input.txt") as f:
        heatmap = list(map(lambda l: l.strip(), f.readlines()))
    return heatmap


def parseGraph(m, reversed=False):
    g = Graph()
    start = None
    target = None
    for row in range(len(m)):
        for col in range(len(m[row])):
            if not g.has_vertex((row, col)):
                v = Vertex((row, col))
                g.add_vertex(v)
            else:
                v = g.get_vertex((row, col))

            if m[row][col] == "S":
                start = v
            if m[row][col] == "E":
                target = v

            add_edge(g, m, v, reversed)

    return g, start, target


def add_edge(g, m, v, reversed=False):
    def can_walk(v, u):
        d = {"S": "a", "E": "z"}
        v = d[v] if v in d else v
        u = d[u] if u in d else u
        if reversed:
            return ord(v) - ord(u) <= 1
        return ord(u) - ord(v) <= 1

    (row, col) = v.key

    for (r, c) in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
        if 0 <= row + r < len(m) and 0 <= col + c < len(m[row]):
            coord = (row + r, col + c)
            u = g.get_vertex(coord)

            if can_walk(m[row][col], m[row + r][col + c]):
                g.add_edge(v, u)


def findCoords(m, pred):
    coords = []
    for row in range(len(m)):
        for col in range(len(m[row])):
            if pred(row, col):
                coords.append((row, col))
    return coords


def solve():
    m = parse()
    # ================PART 1================
    g, start, target = parseGraph(m)
    dijkstra(g, start)
    part1 = len(retrace_path(target, []))

    # ================PART 2================
    coords = findCoords(m, lambda r, c: m[r][c] == "a" or m[r][c] == "S")
    g, _, target = parseGraph(m, True)
    dijkstra(g, target)
    pathss = [retrace_path(g.get_vertex(c), []) for c in coords]
    part2 = min(filter(lambda x: x > 1, map(len, pathss)))

    return {"part 1": part1, "part 2": part2}


pp.pprint(solve())

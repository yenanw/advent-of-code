import re

from math import lcm


Map = dict[str, tuple[str, str]]


def parse(filename: str) -> tuple[str, Map]:
    with open(filename) as f:
        data = f.read()
        ins_obj = re.match(r"(:?R|L)+", data)
        map_obj = re.findall(r"([A-Z]{3})\s=\s\(([A-Z]{3}),\s([A-Z]{3})\)", data)
        if ins_obj is None or len(map_obj) <= 0:
            raise ValueError("fix ye regex goddamnit")

        ins = ins_obj.group(0)
        map: dict[str, tuple[str, str]] = {}
        for start, left, right in map_obj:
            map[start] = (left, right)

        return (ins, map)


def traverse(starts: list[str], ending: str, ins: str, _map: Map) -> int:
    """
    Traverses from all nodes given in `starts` until all start nodes reaches
    a node that ends with `ending` simultaneously. Returns the steps it took
    for that to happen.

    Note 1: if `starts` contains only 1 node, and `ending` == the ending node,
            then it is effectively the same as looking for the amount of steps
            taken from `starts` to `ending`, given the list of instructions.

    Note 2: this does not work for general input because the solution uses LCM
            to speed up the process, since the brute-force approach takes too
            long time. The LCM approach requires the input to "loop", meaning
            if s is the starting node and e1 is the ending node, then the steps
            taken from s -> e1 needs to be same as e1 -> e2, where e2 is the
            an ending node that fulfulls the criteria (i.e. e2 == e1 or that e2
            ends with `ending`), and so on for all ending nodes.
    """
    steps: list[int] = []
    counter = 0
    while len(starts) > 0:
        for i in ins:
            new_starts: list[str] = []
            counter += 1
            for start in starts:
                match i:
                    case "L":
                        next = _map[start][0]
                    case "R":
                        next = _map[start][1]
                    case _:
                        raise ValueError("instructions contains more than L/R")
                if next.endswith(ending):
                    steps.append(counter)
                    continue
                new_starts.append(next)
            starts = new_starts
    return lcm(*steps)


def main() -> None:
    (ins, _map) = parse("input.txt")
    print("Part 1:", traverse(["AAA"], "ZZZ", ins, _map))

    ends_with_A = list(filter(lambda n: n.endswith("A"), _map.keys()))
    print("Part 2:", traverse(ends_with_A, "Z", ins, _map))


if __name__ == "__main__":
    main()

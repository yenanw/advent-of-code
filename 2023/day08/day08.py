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


def read_instructions(start: str, instructions: str, _map: Map) -> str:
    curr = start
    for ins in instructions:
        match ins:
            case "L":
                next = _map[curr][0]
            case "R":
                next = _map[curr][1]
            case _:
                raise ValueError("instructions contains more than L/R")
        curr = next
    return curr


def traverse_single(start: str, end: str, ins: str, _map: Map) -> int:
    steps = 0

    while True:
        curr_end = read_instructions(start, ins, _map)
        start = curr_end
        steps += len(ins)
        if curr_end == end:
            return steps


# def traverse_all(starts: set[str], ends: set[str], ins: str, _map: Map) -> int:
#     steps: list[int] = []
#     for s in starts:
#         all_steps = map(lambda e: traverse_single(s, e, ins, _map), ends)
#         steps.append(min(all_steps))
#     return lcm(*steps)


def traverse_all(starts: set[str], ending: str, ins: str, _map: Map) -> int:
    steps: list[int] = []
    counter = 0
    while len(starts) > 0:
        for i in ins:
            new_starts = set()
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
                new_starts.add(next)
            starts = new_starts
    return lcm(*steps)


def find_all_ends_with(end: str, _map: Map) -> set[str]:
    res: set[str] = set()
    for key in _map:
        if key.endswith(end):
            res.add(key)
    return res


def main() -> None:
    (ins, _map) = parse("input.txt")
    print("Part 1:", traverse_single("AAA", "ZZZ", ins, _map))

    ends_with_A = find_all_ends_with("A", _map)
    ends_with_Z = find_all_ends_with("Z", _map)
    print(ends_with_A)
    print(ends_with_Z)
    print("Part 2:", traverse_all(ends_with_A, "Z", ins, _map))


if __name__ == "__main__":
    main()

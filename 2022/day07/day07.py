import pprint as pp
from parse import compile


def parse():
    # thanks, i hate this too
    cd = compile("$ cd {}")
    file = compile("{:d} {}")
    folder = compile("dir {}")
    file_system = {}
    paths = []
    with open("input.txt") as f:
        for line in f:
            line = line.strip()
            res = cd.parse(line)
            if res is not None:
                res = res[0]
                if res == "..":
                    prev = paths.pop()
                    file_system = prev
                    continue

                paths.append(file_system)
                if res not in file_system.keys():
                    file_system[res] = {}
                file_system = file_system[res]
                continue

            res = file.parse(line)
            if res is not None:
                file_system[res[1]] = int(res[0])
                continue

            res = folder.parse(line)
            if res is not None:
                file_system[res[0]] = {}
    return paths[0]


def dir_size(d):
    s = 0
    for v in d.values():
        s += v if type(v) == int else dir_size(v)
    return s


def flatten(fs, arr):
    arr.append(dir_size(fs))
    for v in fs.values():
        if type(v) == dict:
            flatten(v, arr)
    return arr


def solve():
    fs = parse()

    fs_flat = flatten(fs, [])
    part1 = sum(filter(lambda n: n <= 100000, fs_flat))

    fs_flat.sort()
    total_size = fs_flat[len(fs_flat) - 1]
    part2 = filter(lambda n: 70000000 - total_size + n >= 30000000, fs_flat).__next__()

    return {"part 1": part1, "part 2": part2}


pp.pprint(solve())

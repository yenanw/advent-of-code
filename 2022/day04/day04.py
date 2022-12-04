def parse():
    pairs = []
    with open("input.txt") as f:
        for line in f:
            pairs.append(list(map(lambda p: tuple(map(int, p.split("-"))), line.strip().split(","))))
    return pairs


def contained(i1, i2):
    s1, e1 = i1
    s2, e2 = i2
    return (s1 <= s2 and e1 >= e2) or (s1 >= s2 and e1 <= e2)


def overlaps(i1, i2):
    s1, e1 = i1
    s2, e2 = i2
    return min(e1, e2) - max(s1, s2) >= 0


def solve():
    pairs = parse()

    part1 = sum(map(lambda p: 1 if contained(p[0], p[1]) else 0, pairs))
    part2 = sum(map(lambda p: 1 if overlaps(p[0], p[1]) else 0, pairs))
    return {"part 1": part1, "part 2": part2}


print(solve())

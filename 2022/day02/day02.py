import pprint as pp


win, draw, lose = 6, 3, 0
rock, paper, scissors = 1, 2, 3


def to_rps(p):
    if p == "A" or p == "X":
        return rock
    if p == "B" or p == "Y":
        return paper
    if p == "C" or p == "Z":
        return scissors
    return p


def round(players):
    p2, p1 = players
    res = check(p1, p2)
    return res + to_rps(p1)


def rig_round(players):
    p2, out = players
    p1 = check_rigged(p2, out)
    return round((p2, p1))


def to_out(out):
    if out == "X":
        return lose
    if out == "Y":
        return draw
    if out == "Z":
        return win

    return None


def check_rigged(p, out):
    out = to_out(out)
    p = to_rps(p)
    if out == draw:
        return p

    if p == rock:
        if out == lose:
            return scissors
        if out == win:
            return paper
    if p == paper:
        if out == lose:
            return rock
        if out == win:
            return scissors
    if p == scissors:
        if out == lose:
            return paper
        if out == win:
            return rock


def check(p1, p2):
    p1 = to_rps(p1)
    p2 = to_rps(p2)
    if p1 == p2:
        return draw

    if (
        (p1 == rock and p2 == scissors)
        or (p1 == scissors and p2 == paper)
        or (p1 == paper and p2 == rock)
    ):
        return win

    return lose


def parse(fname):
    strategies = []
    with open(fname) as f:
        for line in f:
            p1, p2 = line.split()
            strategies.append((p1, p2))

    return strategies


def solve():
    strategies = parse("input.txt")

    part1 = sum(map(lambda p: round(p), strategies))
    part2 = sum(map(lambda p: rig_round(p), strategies))

    return {"part 1": part1, "part 2": part2}


pp.pp(solve())

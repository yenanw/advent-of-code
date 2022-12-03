def priority(chr):
    if len(chr) != 1:
        raise ValueError("shit's not a char bro")

    if chr.islower():
        return ord(chr) - 96
    return ord(chr) - 38


def find_mistake(sack):
    comp1, comp2 = set(sack[: len(sack) // 2]), set(sack[len(sack) // 2 :])
    for c in comp1:
        if c in comp2:
            return priority(c)
    return None


def find_badge(s1, s2, s3):
    s1, s2, s3 = map(set, (s1, s2, s3))
    for c in s1:
        if c in s2 and c in s3:
            return priority(c)
    return None


def parse():
    sacks = []
    with open("input.txt") as f:
        for line in f:
            sacks.append(line.strip())
    return sacks


def solve():
    sacks = parse()

    part1 = sum(map(find_mistake, sacks))
    part2 = 0
    for i in range(len(sacks))[::3]:
        part2 += find_badge(sacks[i], sacks[i + 1], sacks[i + 2])
    return {"part 1": part1, "part 2": part2}


print(solve())

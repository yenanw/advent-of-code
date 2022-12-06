def parse():
    with open("input.txt") as f:
        chars = f.readline()
    return chars


def marker(chars, num=4):
    for i in range(num, len(chars)):
        if len(set(chars[i - num : i])) == num:
            return i
    return -1


def solve():
    chars = parse()
    part1 = marker(chars)
    part2 = marker(chars, 14)
    return {"part 1": part1, "part 2": part2}


print(solve())

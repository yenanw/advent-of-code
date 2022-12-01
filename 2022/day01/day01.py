import pprint as pp


def parse(fname):
    elves = []
    with open(fname) as f:
        calories = []
        for line in f:
            try:
                v = int(line.strip())
                calories.append(v)
            except ValueError:
                elves.append(calories)
                calories = []

        if len(calories) != 0:
            elves.append(calories)

    return elves


def solve():
    elves = parse("input.txt")
    total_calories = list(map(sum, elves))
    total_calories.sort(reverse=True)

    part1 = total_calories[0]
    part2 = sum(total_calories[0:3])

    return {"part 1": part1, "part 2": part2}


pp.pp(solve())

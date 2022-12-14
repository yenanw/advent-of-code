import pprint as pp


def parse():
    paths = []
    with open("input.txt") as f:
        for line in f:
            coords = line.strip().split("->")
            path = []
            for coord in coords:
                x, y = coord.strip().split(",")
                path.append((int(x), int(y)))
            paths.append(path)
    return paths


def get_rocks(paths):
    rocks = set()
    for path in paths:
        for i in range(len(path) - 1):
            (sx, sy) = path[i]
            (ex, ey) = path[i + 1]

            dx = 1 if ex >= sx else -1
            dy = 1 if ey >= sy else -1
            for x in range(sx, ex + dx, dx):
                for y in range(sy, ey + dy, dy):
                    rocks.add((x, y))
    return rocks


def get_abyss(rocks):
    return max(map(lambda coord: coord[1], rocks))


def fall_sand(rocks, abyss, at, floored=False):
    floor = abyss + 2
    move = at
    # this better terminates
    while True:
        (x, y) = move
        if not floored and y > abyss:
            return None

        ny = y + 1
        for d in (0, -1, 1):
            if (x + d, ny) not in rocks and ny < floor:
                move = (x + d, ny)
                break

            if d == 1:
                return move


def simulate_sands(rocks, floored=False):
    abyss = get_abyss(rocks)
    source = (500, 0)
    unit_count = 0
    while True:
        res = fall_sand(rocks, abyss, source, floored)
        if res is None:
            break

        rocks.add(res)
        unit_count += 1

        if floored and res == source:
            break

    return unit_count


def solve():
    paths = parse()

    part1 = simulate_sands(get_rocks(paths))
    part2 = simulate_sands(get_rocks(paths), True)
    return {"part 1": part1, "part 2": part2}


pp.pprint(solve())

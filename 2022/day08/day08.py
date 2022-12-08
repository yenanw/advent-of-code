import numpy


def parse():
    trees = []
    with open("input.txt") as f:
        for line in f:
            trees.append(list(map(int, line.strip())))
    return trees


def isVisible(x, y, m):
    if x == 0 or y == 0 or x == len(m[0]) - 1 or y == len(m) - 1:
        return True

    v = m[y][x]
    less = lambda c: c < v
    m_ = numpy.transpose(m)

    return any(
        [
            all(map(less, m[y][:x])),
            all(map(less, m[y][x + 1 :])),
            all(map(less, m_[x][:y])),
            all(map(less, m_[x][y + 1 :])),
        ]
    )


def count_visible(m):
    count = 0
    for y in range(1, len(m) - 1):
        for x in range(1, len(m[0]) - 1):
            count += 1 if isVisible(x, y, m) else 0
    return count + (len(m) + len(m[0]) - 4) * 2 + 4


def scenery_score(x, y, m):
    v = m[y][x]

    views = [0] * 4
    for i in reversed(range(0, x)):
        views[0] += 1
        if m[y][i] >= v:
            break
    for i in range(x + 1, len(m[y])):
        views[1] += 1
        if m[y][i] >= v:
            break
    for i in reversed(range(0, y)):
        views[2] += 1
        if m[i][x] >= v:
            break
    for i in range(y + 1, len(m)):
        views[3] += 1
        if m[i][x] >= v:
            break

    return views[0] * views[1] * views[2] * views[3]


def best_location(m):
    score = 0
    for y in range(1, len(m) - 1):
        for x in range(1, len(m[0]) - 1):
            s = scenery_score(x, y, m)
            score = s if s > score else score
    return score


def solve():
    trees = parse()

    part1 = count_visible(trees)
    part2 = best_location(trees)

    return {"part 1": part1, "part 2": part2}


print(solve())

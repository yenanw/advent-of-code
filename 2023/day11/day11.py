Coord = tuple[int, int]


def parse(filename: str) -> list[Coord]:
    with open(filename) as f:
        galaxies: list[Coord] = []
        for row, line in enumerate(f.readlines()):
            for col, char in enumerate(line.strip()):
                if char != "#":
                    continue
                galaxies.append((int(row), int(col)))

    return galaxies


def expand(galaxies: list[Coord], rate=2) -> list[Coord]:
    """
    Somewhat convulted function for expanding a list of galaxies, checks the
    rows and columns separately for any gaps and expand all subsequent galaxies
    in that direction if there are gaps.
    """
    temp: list[Coord] = []
    expanded: list[Coord] = []

    rows = sorted(galaxies, key=lambda g: g[0])
    for y in range(len(rows) - 1):
        r1, c1 = rows[y]
        r2, c2 = rows[y + 1]

        if r1 + 1 < r2:
            spacing = (r2 - r1 - 1) * (rate - 1)
            for y1 in range(y + 1, len(rows)):
                r3, c3 = rows[y1]
                rows[y1] = (r3 + spacing, c3)

        temp.append((r1, c1))
    temp.append(rows[-1])

    cols = sorted(temp, key=lambda g: g[1])
    for x in range(len(cols) - 1):
        r1, c1 = cols[x]
        r2, c2 = cols[x + 1]

        if c1 + 1 < c2:
            spacing = (c2 - c1 - 1) * (rate - 1)
            for x1 in range(x + 1, len(cols)):
                r3, c3 = cols[x1]
                cols[x1] = (r3, c3 + spacing)

        expanded.append((r1, c1))
    expanded.append(cols[-1])
    return expanded


def sum_shortest_distances(galaxies: list[Coord]) -> int:
    """
    It's just Manhattan distance, yeah...
    """
    dist_sum = 0
    for i in range(len(galaxies)):
        for j in range(i + 1, len(galaxies)):
            x1, y1 = galaxies[i]
            x2, y2 = galaxies[j]
            dist_sum += abs(x2 - x1) + abs(y2 - y1)
    return dist_sum


def main() -> None:
    galaxies = parse("input.txt")

    # ==========================#
    expanded_galaxies = expand(galaxies)
    print("Part 1:", sum_shortest_distances(expanded_galaxies))

    # ==========================#
    expanded_galaxies = expand(galaxies, 1_000_000)
    print("Part 2:", sum_shortest_distances(expanded_galaxies))


if __name__ == "__main__":
    main()

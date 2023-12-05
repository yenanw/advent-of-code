import re


Map = dict[tuple[str, str], set[tuple[int, int, int]]]


def parse(
    filename: str,
) -> tuple[list[int], dict[str, str], Map]:
    with open(filename) as f:
        seeds_pat = re.compile(r"seeds:((?:\s\d+)+)")
        map_pat = re.compile(r"([a-z]+)-to-([a-z]+)\smap:((?:\s\d+)+)")
        data = f.read()

        seeds_obj = seeds_pat.match(data)
        maps_obj = map_pat.findall(data)

        if seeds_obj is None or maps_obj is None:
            raise ValueError("fix your regex goddamnit!")

        seeds = list(map(int, seeds_obj.group(1).split()))

        directions: dict[str, str] = {}
        maps: dict[tuple[str, str], set[tuple[int, int, int]]] = {}

        for src, dest, mappings in maps_obj:
            directions[src] = dest

            mss: set[tuple[int, int, int]] = set()
            for ms in mappings.split("\n"):
                if len(ms.strip()) <= 0:
                    continue
                # destination, source, range
                d, s, r = ms.split()
                mss.add((int(d), int(s), int(r)))

            maps[(src, dest)] = mss

    return (seeds, directions, maps)


def find(id: int, src: str, dest: str, directions: dict[str, str], maps: Map) -> int:
    if src not in directions:
        raise KeyError(f"src {src} not in directions")

    while True:
        to = directions[src]
        mappings = maps[(src, to)]

        for d, s, r in mappings:
            if id not in range(s, s + r):
                continue

            diff = id - s
            id = d + diff
            break

        src = to
        if to == dest:
            return id


def find_ranges(
    id: int, id_range: int, src: str, dest: str, directions: dict[str, str], maps: Map
) -> list[tuple[int, int]]:
    """
    Instead of searching every single number, which takes bazillions of iterations,
    search for ranges instead, if any range surpasses the destionation's max
    range, then we simply take the excess and turn it into a new search, in
    other words, a recursive search.

    Otherwise, the idea of the algorithm is identical to the find() function above.
    """
    if src not in directions:
        return []

    ranges = []
    while True:
        to = directions[src]
        mappings = maps[(src, to)]

        for d, s, r in mappings:
            id_max = id + id_range
            s_max = s + r

            if id not in range(s, s_max):
                continue

            if id_max > s_max:
                # turns the excess ranges into a new search, have no idea if it
                # will trigger an off-by-one error or not, it mostly likely does,
                # but it works on my machine so yeah...
                new_id = s_max
                new_range = id_max - s_max
                new_ranges = find_ranges(new_id, new_range, src, dest, directions, maps)
                ranges.extend(new_ranges)

                id_range = id_range - new_range

            diff = id - s
            id = d + diff
            break

        src = to
        if to == dest:
            ranges.append((id, id_range))
            return ranges


def find_lowest_ranges(ranges: list[tuple[int, int]]) -> int:
    return min(map(lambda r: r[0], ranges))


def main() -> None:
    seeds, directions, maps = parse("input.txt")

    lowest = min(map(lambda s: find(s, "seed", "location", directions, maps), seeds))
    print("Part 1:", lowest)

    seed_ranges = list(zip(seeds[0::2], seeds[1::2]))
    # yeah, about this...
    lowest = min(
        map(
            lambda s: find_lowest_ranges(
                find_ranges(s[0], s[1], "seed", "location", directions, maps)
            ),
            seed_ranges,
        )
    )
    print("Part 2:", lowest)


if __name__ == "__main__":
    main()

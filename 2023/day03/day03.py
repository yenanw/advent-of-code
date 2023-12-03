from collections import deque
from dataclasses import dataclass
from functools import reduce


@dataclass
class Coord:
    x: int
    y: int

    def __hash__(self) -> int:
        return hash((self.x, self.y))

    def __eq__(self, __value: object) -> bool:
        if not isinstance(__value, self.__class__):
            return False

        return (self.x, self.y) == (__value.x, __value.y)

    def __repr__(self) -> str:
        return f"({self.x}, {self.y})"


def parse(filename: str) -> tuple[dict[Coord, int], dict[Coord, str]]:
    with open(filename) as f:
        numbers: dict[Coord, int] = {}
        symbols: dict[Coord, str] = {}

        for i, line in enumerate(f.readlines()):
            for j, c in enumerate(line.strip()):
                coord = Coord(i, j)
                if c.isdigit():
                    numbers[coord] = int(c)
                elif c != ".":
                    symbols[coord] = c

    return (numbers, symbols)


def get_number_on(
    coord: Coord, numbers: dict[Coord, int]
) -> tuple[int, frozenset[Coord]]:
    if coord not in numbers:
        raise KeyError(f"given coordinate {coord} is not in the numbers dictionary")

    (x, y) = coord.x, coord.y
    ranges: set[Coord] = set([coord])
    nums: deque[int] = deque([numbers[coord]])
    left_y = y
    right_y = y
    while True:
        left = Coord(x, left_y - 1)
        right = Coord(x, right_y + 1)
        if left not in numbers and right not in numbers:
            break

        if left in numbers:
            left_y -= 1
            ranges.add(left)
            nums.appendleft(numbers[left])

        if right in numbers:
            right_y += 1
            ranges.add(right)
            nums.append(numbers[right])

    num = reduce(lambda n1, n2: n1 * 10 + n2, nums, 0)
    return (num, frozenset(ranges))


def neighbors(coord: Coord) -> set[Coord]:
    x, y = coord.x, coord.y
    coords: set[Coord] = set()
    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            coords.add(Coord(x + i, y + j))

    return coords


def get_part_numbers(
    numbers: dict[Coord, int], symbols: dict[Coord, str]
) -> list[tuple[int, frozenset[Coord]]]:
    part_numbers: list[tuple[int, frozenset[Coord]]] = []
    for c in symbols:
        for n in neighbors(c):
            if n not in numbers:
                continue

            res = get_number_on(n, numbers)
            if res not in part_numbers:
                part_numbers.append(res)

    return part_numbers


def get_gears(
    numbers: dict[Coord, int], symbols: dict[Coord, str]
) -> set[tuple[Coord, int]]:
    gears: set[tuple[Coord, int]] = set()
    for c, s in symbols.items():
        part_numbers: list[tuple[int, frozenset[Coord]]] = []
        if s != "*":
            continue

        for n in neighbors(c):
            if n not in numbers:
                continue

            res = get_number_on(n, numbers)
            if res not in part_numbers:
                part_numbers.append(res)

        if len(part_numbers) == 2:
            gears.add((c, part_numbers[0][0] * part_numbers[1][0]))
    return gears


def main() -> None:
    numbers, symbols = parse("input.txt")

    part_numbers = map(lambda t: t[0], get_part_numbers(numbers, symbols))
    print("Part 1:", sum(part_numbers))

    gears = map(lambda t: t[1], get_gears(numbers, symbols))
    print("Part 2:", sum(gears))


if __name__ == "__main__":
    main()

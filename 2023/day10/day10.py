from collections import deque

Coord = tuple[int, int]


def parse(filename: str) -> tuple[Coord, tuple[int, int], dict[Coord, list[Coord]]]:
    with open(filename) as f:
        lines = f.readlines()
        row_max = len(lines) - 1
        col_max = len(lines[0].strip()) - 1  # assuming map is rectangular
        adj_list: dict[Coord, list[Coord]] = {}
        symbols: dict[Coord, str] = {}
        starting: Coord = (0, 0)
        for row, line in enumerate(lines):
            for col, char in enumerate(line.strip()):
                if char != ".":
                    symbols[(row, col)] = char

                neighbors: list[Coord] = []
                match char:
                    case "|":
                        neighbors.append((row + 1, col))
                        neighbors.append((row - 1, col))
                    case "-":
                        neighbors.append((row, col + 1))
                        neighbors.append((row, col - 1))
                    case "L":
                        neighbors.append((row - 1, col))
                        neighbors.append((row, col + 1))
                    case "J":
                        neighbors.append((row - 1, col))
                        neighbors.append((row, col - 1))
                    case "7":
                        neighbors.append((row + 1, col))
                        neighbors.append((row, col - 1))
                    case "F":
                        neighbors.append((row + 1, col))
                        neighbors.append((row, col + 1))
                    case ".":
                        continue
                    case "S":
                        starting = (row, col)
                    case c:
                        raise ValueError(f"character {c} not a valid input!")

                fixed_neighbors: list[Coord] = []
                for r, _c in neighbors:
                    if 0 <= r <= row_max and 0 <= _c <= col_max:
                        fixed_neighbors.append((r, _c))

                if fixed_neighbors:
                    adj_list[(row, col)] = fixed_neighbors

        adj_list[starting] = []
        for coord in adj_list:
            if coord == starting:
                continue

            neighbors = adj_list[coord]
            if starting in neighbors:
                adj_list[starting].append(coord)

        return starting, (row_max, col_max), adj_list


def count_steps(starting: Coord, pipes: dict[Coord, list[Coord]]) -> dict[Coord, int]:
    visited: set[Coord] = set([starting])
    coords: deque[Coord] = deque([starting])
    depths: deque[int] = deque([0])
    steps_from_start: dict[Coord, int] = {}

    while len(coords) > 0:
        pipe = coords.popleft()
        depth = depths.popleft()
        steps_from_start[pipe] = depth

        neighbors = pipes[pipe] if pipe in pipes else []

        for n in neighbors:
            if n in visited:
                continue

            visited.add(n)
            coords.append(n)
            depths.append(depth + 1)

    return steps_from_start


def is_inside(point: Coord, loop_coords: dict[Coord, list[Coord]]) -> bool:
    """
    Ray casting technology right here

    The idea is to shoot a ray from top to bottom, check the amount of collisions,
    if the collision count is even then it's outside, otherwise inside.

    Kinda finicky since some walls in the loop can be vertical, which need
    special conditions to decide whether it collides or not.

    The condition is calculated as following:
        1. if encountered a horizontal wall "-", then it collides
        2. else if encountered a veritical wall "|", ignore it
        3. else if encountered any wall that goes downwards, i.e. "F" or "7",
           then keep going until "-", "J" or "L".
            3a. check if the horizontal directions are the same, i.e. "F" and "L"
               have the same horizontal directions, as well as "7" and "J"
            3b. if the directions are the same, then ignore it as the ray does
                not collide with it, else it must collides

    TODO: probably clean up this code, if I ever feel like doing it...
    """
    x, y = point

    collisions = 0
    i = -1
    while i < x:
        i += 1
        if (i, y) not in loop_coords:
            continue

        neighbors = loop_coords[(i, y)]
        if (i + 1, y) not in neighbors:
            collisions += 1
            continue

        other = list(filter(lambda n: n[1] != y, neighbors))
        if len(other) <= 0:
            continue

        (_, start_y) = other[0]
        while i < x:
            i += 1
            if (i, y) not in loop_coords:
                continue

            neighbors = loop_coords[(i, y)]
            if (i + 1, y) in neighbors:
                continue

            other = list(filter(lambda n: n[1] != y, neighbors))
            (_, end_y) = other[0]
            if len(other) == 2 or start_y != end_y:
                collisions += 1
            break

    return collisions % 2 != 0


def count_insides(
    max_row: int, max_col: int, loop_coords: dict[Coord, list[Coord]]
) -> int:
    count = 0
    for row in range(0, max_row):
        for col in range(0, max_col):
            if (row, col) in loop_coords:
                continue

            if is_inside((row, col), loop_coords):
                count += 1
    return count


def main() -> None:
    start, (max_row, max_col), pipes = parse("input.txt")

    # ==========================#
    steps_from_start = count_steps(start, pipes)
    print("Part 1:", max(steps_from_start.values()))

    # ==========================#
    loop_coords = {k: pipes[k] for k in steps_from_start}
    print("Part 2:", count_insides(max_row, max_col, loop_coords))


if __name__ == "__main__":
    main()

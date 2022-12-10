def parse():
    moves = []
    with open("input.txt") as f:
        for line in f:
            direction, step = line.strip().split(" ")
            moves.append((direction, int(step)))
    return moves


def is_touching(head, tail):
    yd = abs(head[1] - tail[1])
    xd = abs(head[0] - tail[0])
    return yd + xd < 2 or yd == xd == 1


def catch_up(head, tail):
    hx, hy = head
    tx, ty = tail
    # move the tail based on the previous knot's position,
    # !!!NOT THE MOVE DIRECTION!!!
    xd = 0 if hx == tx else (1 if hx > tx else -1)  # ternary is nice eh
    yd = 0 if hy == ty else (1 if hy > ty else -1)
    return (tx + xd, ty + yd)


def move_one(head, tail, direction, move_head=True):
    (hx, hy) = head
    if move_head:
        match direction:
            case "U":
                head = (hx, hy + 1)
            case "D":
                head = (hx, hy - 1)
            case "R":
                head = (hx + 1, hy)
            case "L":
                head = (hx - 1, hy)

    if is_touching(head, tail):
        return (head, tail)

    return (head, catch_up(head, tail))


def move_all(moves, num=2):
    knots = [(0, 0)] * num
    visited = set({})

    for (direction, step) in moves:
        for _ in range(step):
            # move the head first
            knots[0], knots[1] = move_one(knots[0], knots[1], direction)
            for i in range(1, num - 1):
                # move all other knots according to their previous knots position
                # without moving the previous knot like the head
                k1, k2 = move_one(knots[i], knots[i + 1], direction, False)
                knots[i] = k1
                # if the later knot hasn't moved then no other knot will move
                if knots[i + 1] == k2:
                    break
                knots[i + 1] = k2
            # store all visited coords of the tail
            visited.add(knots[num - 1])
    return (knots, visited)


def solve():
    moves = parse()

    part1 = len(move_all(moves)[1])
    part2 = len(move_all(moves, 10)[1])
    return {"part 1": part1, "part 2": part2}


print(solve())

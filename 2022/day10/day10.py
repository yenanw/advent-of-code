import pprint as pp


def parse():
    instrs = []
    with open("input.txt") as f:
        for line in f:
            line = line.strip()
            if line == "noop":
                instrs.append((line, None))
                continue
            add, num = line.split(" ")
            instrs.append((add, int(num)))
    return instrs


def run_prog(instrs):
    cycle = 0
    x = 1
    states = []
    for (ins, arg) in instrs:
        cycle += 1
        states.append((x, cycle))
        match ins:
            case "noop":
                continue
            case "addx":
                cycle += 1
                states.append((x, cycle))
                x += arg

    states.append((x, cycle + 1))
    return states


def draw_sprite(states):
    if len(states) < 40:
        return ""

    curr_row = ""
    for i in range(len(states)):
        (x, _) = states[i]
        if i in (x - 1, x, x + 1):
            curr_row += "#"
        else:
            curr_row += "."
    return curr_row


def solve():
    instrs = parse()
    states = run_prog(instrs)

    part1 = sum([s[0] * s[1] for s in states[19:220:40]])
    part2 = "\n".join(map(draw_sprite, [states[i : i + 40] for i in range(0, len(states), 40)]))

    return {"Part 1": part1, "Part 2": part2}


pp.pprint(solve())

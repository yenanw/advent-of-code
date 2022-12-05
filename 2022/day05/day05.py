from parse import compile
from copy import deepcopy


def parse():
    # i hate this
    stacks = [[] for _ in range(9)]
    instructions = []
    stack_pattern = compile(("[{}]"))
    ins_pattern = compile("move {:d} from {:d} to {:d}")
    with open("input.txt") as f:
        for i, line in enumerate(f):
            if i < 9:
                for j in range(0, 9):
                    container = stack_pattern.search(line[j * 4 : j * 4 + 3])
                    if container is None:
                        continue
                    stacks[j].insert(0, container[0])
                continue
            ins = ins_pattern.search(line)
            if ins is None:
                continue
            instructions.append(ins)
    return (stacks, instructions)


def read_instructions(stacks, inss, one_at_a_time=True):
    stacks = deepcopy(stacks)
    for (move, fr, to) in inss:
        cs = stacks[fr - 1][len(stacks[fr - 1]) - move :]
        if one_at_a_time:
            cs.reverse()

        stacks[fr - 1] = stacks[fr - 1][: len(stacks[fr - 1]) - move]
        stacks[to - 1].extend(cs)
        continue
    return stacks


def solve():
    (stacks, inss) = parse()
    part1 = "".join(map(lambda stack: stack[len(stack) - 1], read_instructions(stacks, inss)))
    part2 = "".join(map(lambda stack: stack[len(stack) - 1], read_instructions(stacks, inss, False)))
    return {"part 1": part1, "part 2": part2}


print(solve())

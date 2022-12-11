import parse as prs
import pprint as pp

from copy import deepcopy
from functools import reduce
from operator import mul


def parse():
    monkeys = {}
    with open("input.txt") as f:
        for sec in f.read().strip().split("\n\n"):
            i, items, op, num, test, t, f = prs.parse(
                """Monkey {:d}:
  Starting items: {}
  Operation: new = old {} {}
  Test: divisible by {:d}
    If true: throw to monkey {:d}
    If false: throw to monkey {:d}""",
                sec,
            )
            monkeys[i] = {
                "items": list(map(int, items.split(","))),
                "op": {"operator": op, "operand": num},
                "test": {"by": test, "true": t, "false": f},
                "inspect_count": 0,
            }
    return monkeys


def monkey_throw(monkeys, mod, i, super_duper_stressed=False):
    monkey = monkeys[i]
    items = monkey["items"]
    while len(items) > 0:
        op = monkey["op"]
        test = monkey["test"]
        worry = items.pop(0)

        operand = op["operand"] if op["operand"] != "old" else worry
        # you should use as much eval() as possible, it's good /s
        worry = eval(f"{str(worry)} {op['operator']} {operand}")
        if not super_duper_stressed:
            worry //= 3
        worry %= mod

        to = test["true"] if worry % test["by"] == 0 else test["false"]
        monkeys[to]["items"].append(worry)
        monkey["inspect_count"] += 1


def play_round(monkeys, mod, num, stressed=False):
    for n in range(num):
        for i in range(len(monkeys)):
            monkey_throw(monkeys, mod, i, stressed)
        if (n + 1) % (num // 5) == 0:
            print(f"Finished round {n+1}")


def monke_interaction(monkeys, r, stressed=False):
    m = deepcopy(monkeys)
    mod = reduce(mul, map(lambda i: monkeys[i]["test"]["by"], monkeys), 1)
    play_round(m, mod, r, stressed)
    l = list(map(lambda i: m[i]["inspect_count"], m))
    l.sort(reverse=True)
    return l[0] * l[1]


def solve():
    monkeys = parse()

    print("==========COMPUTING PART 1==========")
    part1 = monke_interaction(monkeys, 20, False)
    print("==========COMPUTING PART 2==========")
    part2 = monke_interaction(monkeys, 10000, True)
    return {"part 1": part1, "part 2": part2}


pp.pprint(solve())

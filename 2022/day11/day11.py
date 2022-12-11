import parse as prs
import pprint as pp
import sympy

from functools import reduce
from operator import mul


def parse():
    monkeys = {}
    tests = set()
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
            tests.add(test)
    return monkeys, tests


def monkey_throw(monkeys, tests, i):
    monkey = monkeys[i]
    items = monkey["items"]
    while len(items) > 0:
        op = monkey["op"]
        test = monkey["test"]
        worry = items.pop(0)

        operand = op["operand"] if op["operand"] != "old" else worry
        # worry = eval(f"{str(worry)} {op['operator']} {operand}") // 3
        worry = eval(f"{str(worry)} {op['operator']} {operand}")

        to = test["true"] if worry % test["by"] == 0 else test["false"]
        #worry = remove_factors(tests, worry)
        worry %= tests
        monkeys[to]["items"].append(worry)
        monkey["inspect_count"] += 1


def remove_factors(tests, num):
    factors = sympy.factorint(num)

    prod = 1
    for k in factors.keys():
        if k in tests:
            factors[k] -= 1
        if factors[k] <= 0:
            continue

        prod *= k ** factors[k]

    return num // prod


def play_round(monkeys, tests, num):
    for n in range(num):
        for i in range(len(monkeys)):
            monkey_throw(monkeys, tests, i)        


def solve():
    monkeys, tests = parse()
    tests = reduce(mul, tests, 1)
    print(tests)

    play_round(monkeys, tests, 20)
    print(monkeys)
    l = list(map(lambda i: monkeys[i]["inspect_count"], monkeys))
    l.sort(reverse=True)
    part1 = l[0] * l[1]

    play_round(monkeys, tests, 9980)

    l = list(map(lambda i: monkeys[i]["inspect_count"], monkeys))
    l.sort(reverse=True)
    part2 = l[0] * l[1]
    return {"part 1": part1, "part 2": part2}


pp.pprint(solve())

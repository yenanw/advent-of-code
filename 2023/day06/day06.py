from math import prod, sqrt, ceil, floor, isclose
import re


def parse(filename: str) -> list[tuple[int, int]]:
    with open(filename) as f:
        data = f.read()
        time_data = re.search(r"Time:\s*((?:\s*\d+)+)", data)
        distance_data = re.search(r"Distance:\s*((?:\s*\d+)+)", data)

        if time_data is None or distance_data is None:
            raise ValueError("fix your regex goddamnit!")

        times = list(map(int, time_data.group(1).split()))
        distances = list(map(int, distance_data.group(1).split()))

        return list(zip(times, distances))


def find_margins(time: int, record_distance: int) -> tuple[int, int]:
    """
    f(x) = x * (C_1 - x), where C_1 is the time
    C_2 = C_1 * x - x^2, where C_2 is the record_distance
    x^2 - C_1 * x + C_2 = 0 -> solve for the margins

    At least it's not calculus amirite, ha ha... ha
    """
    x1 = time / 2 - sqrt((time / 2) ** 2 - record_distance)
    x2 = time / 2 + sqrt((time / 2) ** 2 - record_distance)

    # sometimes the answers can be exact, so we need to +/-1
    if isclose(x1, int(x1)):
        x1 += 1
    if isclose(x2, int(x2)):
        x2 -= 1

    return (ceil(x1), floor(x2))


def fix_input(parsed: list[tuple[int, int]]) -> tuple[int, int]:
    times, records = zip(*parsed)

    time = int("".join(map(str, times)))
    record = int("".join(map(str, records)))

    return (time, record)


def main() -> None:
    records = parse("input.txt")

    print(
        prod(
            map(
                lambda x: x[1] - x[0] + 1,
                map(lambda x: find_margins(x[0], x[1]), records),
            )
        )
    )

    t, d = fix_input(records)
    x1, x2 = find_margins(t, d)
    print(x2 - x1 + 1)


if __name__ == "__main__":
    main()

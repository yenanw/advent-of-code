import re

num_dict: dict[str, int] = {
    "one": 1,
    "two": 2,
    "three": 3,
    "four": 4,
    "five": 5,
    "six": 6,
    "seven": 7,
    "eight": 8,
    "nine": 9,
}


def parse(filename: str, pattern_str: str) -> list[tuple[int, int]]:
    with open(filename) as file:
        digits: list[tuple[int, int]] = []
        pattern = re.compile(pattern_str)
        for line in file.readlines():
            res = re.findall(pattern, line)

            first = res[0]
            last = res[-1]

            first = int(first) if first.isdigit() else num_dict[first]
            last = int(last) if last.isdigit() else num_dict[last]

            digits.append((first, last))

    return digits


def sum_digits(digits: list[tuple[int, int]]) -> int:
    nums: list[int] = []
    for num1, num2 in digits:
        nums.append(num1 * 10 + num2)
    return sum(nums)


def main() -> None:
    print("Part 1:", sum_digits(parse("input.txt", r"\d")))

    # look ahead to enable overlapping searches, i love hacks like this
    part2_regex = r"(?=(one|two|three|four|five|six|seven|eight|nine|\d))"
    print("Part 2:", sum_digits(parse("input.txt", part2_regex)))


if __name__ == "__main__":
    main()

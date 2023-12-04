import re


def parse(filename: str) -> dict[int, tuple[set[int], set[int]]]:
    with open(filename) as f:
        cards: dict[int, tuple[set[int], set[int]]] = {}
        pat = re.compile(r"Card\s+(\d+):\s+((?:\d+\s+)+)\s*\|\s*((?:\d+\s*)+)")
        for line in f.readlines():
            match = pat.match(line)
            if match is None:
                raise ValueError("fix your regex goddamnit!")

            id, winning, owned = match.group(1, 2, 3)
            winning_set = set(map(int, winning.strip().split()))
            owned_set = set(map(int, owned.strip().split()))

            cards[int(id)] = (winning_set, owned_set)

    return cards


def winning_numbers(winning: set[int], owned: set[int]) -> list[int]:
    return list(filter(lambda n: n in owned, winning))


def score(winning: set[int], owned: set[int]) -> int:
    numbers = winning_numbers(winning, owned)
    if len(numbers) <= 0:
        return 0

    return 2 ** (len(numbers) - 1)


def scratch_cards(cards: dict[int, list[int]]) -> dict[int, int]:
    # numbers lower than k are never touched again, so we can just iterate through it
    keys = sorted(cards.keys())
    cards_owned: dict[int, int] = {k: 1 for k in cards}
    for k in keys:
        matching = len(cards[k])
        for i in range(k + 1, k + matching + 1):
            # out of bounds cards here, shouldn't happen if the input is "appropriate"
            if k not in cards_owned:
                cards_owned[k] = 1
                continue
            cards_owned[i] += cards_owned[k]

    return cards_owned


def main() -> None:
    cards = parse("input.txt")

    total_points = sum(map(lambda v: score(v[0], v[1]), cards.values()))
    print("Part 1:", total_points)

    all_winning_nums = {k: winning_numbers(w, o) for k, (w, o) in cards.items()}
    card_counts = scratch_cards(all_winning_nums)
    print("Part 2:", sum(card_counts.values()))


if __name__ == "__main__":
    main()

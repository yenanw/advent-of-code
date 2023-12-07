from collections import deque
from functools import total_ordering

FIVE_OF_A_KIND = 7
FOUR_OF_A_KIND = 6
FULL_HOUSE = 5
THREE_OF_A_KIND = 4
TWO_PAIR = 3
ONE_PAIR = 2
HIGH_CARD = 1


@total_ordering
class Hand:
    def __init__(self, cards: str, joker=False) -> None:
        self.card_order = {
            "A": 13,
            "K": 12,
            "Q": 11,
            "J": 10,
            "T": 9,
            "9": 8,
            "8": 7,
            "7": 6,
            "6": 5,
            "5": 4,
            "4": 3,
            "3": 2,
            "2": 1,
        }

        if joker:
            self.card_order["J"] = 0

        self.cards: deque[str] = deque(maxlen=5)
        self.jokers = set()

        card_count: dict[str, int] = {}
        for i, c in enumerate(cards):
            self.cards.append(c)

            if c == "J":
                self.jokers.add(i)

            if c not in card_count:
                card_count[c] = 0
            card_count[c] += 1

        items = sorted(list(card_count.items()), key=lambda c: c[1])
        match items:
            case [(_, 5)]:
                self.type = FIVE_OF_A_KIND
            case [(a, 1), (b, 4)]:
                self.type = FOUR_OF_A_KIND

                if joker and (a == "J" or b == "J"):
                    self.type = FIVE_OF_A_KIND
            case [(a, 2), (b, 3)]:
                self.type = FULL_HOUSE

                if joker and (a == "J" or b == "J"):
                    self.type = FIVE_OF_A_KIND
            case [(_, 1), (_, 1), (_, 3)]:
                self.type = THREE_OF_A_KIND

                # any J here will maximally yield a four of a kind
                if joker and len(self.jokers) > 0:
                    self.type = FOUR_OF_A_KIND
            case [(a, 1), (b, 2), (c, 2)]:
                self.type = TWO_PAIR

                if not joker:
                    return

                if a == "J":
                    self.type = FULL_HOUSE
                elif b == "J" or c == "J":
                    self.type = FOUR_OF_A_KIND
            case [(_, 1), (_, 1), (_, 1), (_, 2)]:
                self.type = ONE_PAIR

                # any J here will maximally yield a three of a kind
                if joker and len(self.jokers) > 0:
                    self.type = THREE_OF_A_KIND
            case [(_, 1), (_, 1), (_, 1), (_, 1), (_, 1)]:
                self.type = HIGH_CARD

                # any J here will maximally yield an one pair
                if joker and len(self.jokers) > 0:
                    self.type = ONE_PAIR
            case _:
                raise ValueError(f"hand {cards} not valid")

    def __hash__(self) -> int:
        return hash(str(self))

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, self.__class__):
            return False
        return str(self) == str(other)

    def __lt__(self, other: object) -> bool:
        if not isinstance(other, self.__class__):
            raise ValueError(f"{other} is not of type {self.__class__}")

        if self.type != other.type:
            return self.type < other.type

        for c1, c2 in zip(self.cards, other.cards):
            if self.card_order[c1] != self.card_order[c2]:
                return self.card_order[c1] < self.card_order[c2]

        return False

    def __str__(self) -> str:
        return "".join(self.cards)

    def __repr__(self) -> str:
        return str(self)


def parse(filename: str, joker=False) -> list[tuple[Hand, int]]:
    hands: list[tuple[Hand, int]] = []
    with open(filename) as f:
        for line in f.readlines():
            cards, bid = line.split()
            hands.append((Hand(cards, joker), int(bid)))
    return hands


def get_total_winnings(hands: list[tuple[Hand, int]]) -> int:
    sorted_hands = enumerate(sorted(hands, key=lambda x: x[0]))
    return sum(map(lambda h: (h[0] + 1) * h[1][1], sorted_hands))


def main() -> None:
    hands = parse("input.txt")
    print("Part 1:", get_total_winnings(hands))

    hands = parse("input.txt", joker=True)
    print("Part 2:", get_total_winnings(hands))


if __name__ == "__main__":
    main()

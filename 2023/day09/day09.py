from collections import deque
from itertools import repeat


def parse(filename: str) -> list[deque[int]]:
    with open(filename) as f:
        seqs: list[deque[int]] = []
        for line in f.readlines():
            seqs.append(deque(map(int, line.split())))
        return seqs


def extrapolate_history(seq: deque[int], add_front=False) -> int:
    """
    Just brute-force it, I guess...
    """
    curr = deque(seq)
    seqs = [curr]
    while any(curr):
        new_seq: deque[int] = deque([])
        for i in range(len(curr) - 1):
            new_seq.append(curr[i + 1] - curr[i])
        seqs.append(new_seq)
        curr = new_seq

    nums_at = 0 if add_front else -1
    for i in range(len(seqs) - 1, 0, -1):
        n1 = seqs[i][nums_at]
        n2 = seqs[i - 1][nums_at]
        if add_front:
            seqs[i - 1].appendleft(n2 - n1)
        else:
            seqs[i - 1].append(n2 + n1)

    return seqs[0][nums_at]


def main() -> None:
    seqs = parse("input.txt")

    history_sum_1 = sum(map(extrapolate_history, seqs))
    print("Part 1:", history_sum_1)

    history_sum_2 = sum(map(extrapolate_history, seqs, repeat(True)))
    print("Part 2:", history_sum_2)


if __name__ == "__main__":
    main()
